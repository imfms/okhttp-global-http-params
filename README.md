[![](https://jitpack.io/v/imfms/okhttp-global-http-params.svg)](https://jitpack.io/#imfms/okhttp-global-http-params)


# okhttp-global-request-params

An flexible global http request params append tool for Okhttp Interceptor mode

[中文文档](README_CN.md)

`Joke with my poor English`

## Reference Method

- Gradle LatestVersion is [![](https://jitpack.io/v/imfms/okhttp-global-http-params.svg)](https://jitpack.io/#imfms/okhttp-global-http-params)


        repositories {
            maven { url 'https://jitpack.io' } // If not already there
        }
        
        dependencies {
            compile 'com.github.imfms:okhttp-global-http-params:${latest.version}'
        }

## Support Params

- Header Params
- URL query Params
- RequestBody Params
    - FormBody - Support Default implement
    - MulitPartBody - Support Default implement
    - [Custom]
    
## Usage

### Code Sample

~~~java
 new OkHttpClient.Builder()
    .addInterceptor(
            new GlobalHttpParamsIntercepter(new GlobalHttpParamsIntercepter.OnNeedHttpParams() {
                @Override
                public HttpParams getParams(Request request) {
                    return new HttpParams.Builder()
                            // set URL_Query params
                            .setUrlQuerys(
                                    new UrlQuerys.Builder()
                                            .addQueryParameter("local_time", System.currentTimeMillis() + "")
                                            .build()
                            )
                            // set RequestHeader params
                            .setRequestHeaders(
                                    new Headers.Builder()
                                            .add("local_time", System.currentTimeMillis() + "")
                                            .build()
                            )
                            // add RequstBody params (if method support requestbody)
                            .addRequestBody(
                                    new FormBody.Builder()
                                            .add("local_time", System.currentTimeMillis() + "")
                                            .build()
                            )
                            .addRequestBody(
                                    new MultipartBody.Builder()
                                            .addFormDataPart("local_time", System.currentTimeMillis() + "")
                                            .build()
                            )
                            .build();
                }
            })
    )
~~~


### Custom RequestBody Appender

Developer project maybe need custom RequestBody, so default method can't use, library provide custom RequestBody appender.

Default Support FormBodyAppender & MulitPartBodyAppender, if don't need please call clearRequestBodyAppender

#### Write An Custom Appender - RequestBodyAppender
~~~java
/**
 * RequestBody Appender, when need append custom source RequestBody and append RequestBody
 */
public interface RequestBodyAppender {

    /**
     * select this appender accept requestbody type
     *
     * @param sourceBody requestbody
     * @return true -> accept, false -> not accept
     */
    boolean isAccept(RequestBody sourceBody, RequestBody appendBody);

    /**
     * append source RequestBody and appendRequestBody
     *
     * @param source sourceRequestBody
     * @param append appendRequestBody
     * @return appended RequestBody
     */
    RequestBody append(RequestBody source, RequestBody append);
}
~~~

#### Add Custom Appender To GlobalHttpParamsIntercepter
~~~java
    GlobalHttpParamsIntercepter.addRequestBodyAppender(RequestBodyAppender appender);
~~~

**Attention: Developer project maybe need 'when http request need requestbody method but request body be empty(contentLength == 0)' appender gloable params, library can't know developer's contentType and params append method, so if developer need be similar to up content please must custom & specify RequestBodyAppender**

~~~java
public class EmptyRequestBodyAppender implements RequestBodyAppender {

    @Override public boolean isAccept(RequestBody requestBody, RequestBody requestBody1) {

        long contentLength;

        try {
            contentLength = requestBody.contentLength();
        } catch (IOException e) {
            contentLength = -1;
        }

        return contentLength <= 0
                && requestBody1 instanceof FormBody;
    }

    @Override public RequestBody append(RequestBody requestBody, RequestBody requestBody1) {
        return requestBody1;
    }

}
~~~