#version 130

uniform vec2 uResolution;
uniform sampler2D sFrameBuffer;
uniform float uTime;

float hash(in float n)
{
    return fract(sin(n) * 43758.5453123);
}

/*void main()
{
    vec2 p = gl_FragCoord.xy / uResolution.xy;

    vec2 u = p * 2. - 1.;
    vec2 n = u * vec2(uResolution.x / uResolution.y, 1.0);
    vec3 c = texture2D(sFrameBuffer, p).xyz;


    // flicker, grain, vignette, fade in
    c += sin(hash(uTime)) * 0.01;
    c += hash((hash(n.x) + n.y) * uTime) * 0.5;
    c *= smoothstep(length(n * n * n * vec2(0.075, 0.4)), 1.0, 0.4);
    c *= smoothstep(0.001, 3.5, uTime) * 1.5;

    c = dot(c, vec3(0.2126, 0.7152, 0.0722))
    * vec3(0.2, 1.5 - hash(uTime) * 0.1, 0.4);

    gl_FragColor = vec4(c, 1.0);
}**/

float remap(float value, float inputMin, float inputMax, float outputMin, float outputMax)
{
    return (value - inputMin) * ((outputMax - outputMin) / (inputMax - inputMin)) + outputMin;
}
float rand(vec2 n, float time)
{
    return 0.5 + 0.5 *
    fract(sin(dot(n.xy, vec2(12.9898, 78.233)))* 43758.5453 + time);
}

struct Circle
{
    vec2 center;
    float radius;
};

vec4 circle_mask_color(Circle circle, vec2 position)
{
    float d = distance(circle.center, position);
    if(d > circle.radius)
    {
        return vec4(0.0, 0.0, 0.0, 1.0);
    }

    float distanceFromCircle = circle.radius - d;
    float intencity = smoothstep(
    0.0, 1.0,
    clamp(remap(distanceFromCircle, 0.0, 0.1, 0.0, 1.0), 0.0, 1.0));
    return vec4(intencity, intencity, intencity, 1.0);
}

vec4 mask_blend(vec4 a, vec4 b)
{
    vec4 one = vec4(1.0, 1.0, 1.0, 1.0);
    return one - (one - a) * (one - b);
}

float f1(float x)
{
    return -4.0 * pow(x - 0.5, 2.0) + 1.0;
}

void main()
{
    vec2 uv = gl_FragCoord.xy / uResolution.xy;

    float wide = uResolution.x / uResolution.y;
    float high = 1.0;

    vec2 position = vec2(uv.x * wide, uv.y);

    Circle circle_a = Circle(vec2(0.5, 0.5), 0.5);
    Circle circle_b = Circle(vec2(wide - 0.5, 0.5), 0.5);
    vec4 mask_a = circle_mask_color(circle_a, position);
    vec4 mask_b = circle_mask_color(circle_b, position);
    vec4 mask = mask_blend(mask_a, mask_b);

    float greenness = 1;
    vec4 coloring = vec4(1.0 - greenness, 1.0, 1.0 - greenness, 1.0);

    //float noise = rand(uv * vec2(0.1, 1.0), uTime * 5.0);
    //float noiseColor = 1.0 - (1.0 - noise) * 0.3;
    vec2 u = uv * 2. - 1.;
    vec2 n = u * vec2(uResolution.x / uResolution.y, 1.0);
    float noise = sin(hash(uTime)) * 0.01;
    noise += hash((hash(n.x) + n.y) * uTime) * 0.9;
    vec4 noising = vec4(noise, noise, noise, 1.0);

    float warpLine = fract(-uTime * 0.5);

    float warpLen = 0.1;
    float warpArg01 = remap(clamp((position.y - warpLine) - warpLen * 0.5, 0.0, warpLen), 0.0, warpLen, 0.0, 1.0);
    float offset = sin(warpArg01 * 10.0)  * f1(warpArg01);


    vec4 lineNoise = vec4(1.0, 1.0, 1.0, 1.0);
    if(abs(uv.y - fract(-uTime * 19.0)) < 0.0005)
    {
        lineNoise = vec4(0.5, 0.5, 0.5, 1.0);
    }

    //vec4 base = texture(sFrameBuffer, uv + vec2(offset * 0.02, 0.0));
    vec4 base = texture(sFrameBuffer, uv);
    gl_FragColor = base * mask * coloring * noising * lineNoise;
}