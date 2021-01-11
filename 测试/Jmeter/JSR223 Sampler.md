# JMeter JSR223 Sampler

```groovy
import com.aoeai.HttpUtil;

def result = HttpUtil.post("http://test.net", "{}");
println(vars.get("userName") + " 请求结果 " + result);
println("hh    " + SampleResult.getSampleLabel());

SampleResult.setResponseCode("201");
//SampleResult.setSuccessful(false);
SampleResult.setResponseMessage("返回结果 This is message returned from JSR223 script");
SampleResult.setResponseData("看到的结果 You will see this sentence in Response Data tab", "UTF-8");

println( "The Sample Label is : " + SampleResult.getSampleLabel() );
println( "The Start Time in miliseconds is : " + SampleResult.getStartTime() );
println( "The Response Code is : " + SampleResult.getResponseCode() );
println( "The Response Message is : " + SampleResult.getResponseMessage() );
```

- [Best Practices](https://jmeter.apache.org/usermanual/best-practices.html)
- [JSR223 with Groovy: Variables (Part 2)](https://jmetervn.com/2016/12/20/jsr223-with-groovy-variables-part-2/)