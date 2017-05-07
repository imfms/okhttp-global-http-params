package cn.f_ms.network.okhttp.intercepter.globalrequestparams;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * RequestBody_MulitPartBody appender
 *
 * @author f-ms
 * @time 2017/5/7
 */
public class MulitPartBodyAppender implements GlobalHttpParamsIntercepter.RequestBodyAppender {

    private static class Instance {
        static final MulitPartBodyAppender INSTANCE = new MulitPartBodyAppender();
    }

    private MulitPartBodyAppender() {}
    public static MulitPartBodyAppender getInstance() { return Instance.INSTANCE; }

    @Override
    public boolean isAccept(RequestBody sourceBody, RequestBody appendBody) {
        return sourceBody instanceof MultipartBody
                && appendBody instanceof MultipartBody;
    }

    @Override
    public RequestBody append(RequestBody source, RequestBody append) {

        MultipartBody firstMultipartBody = (MultipartBody) source;
        MultipartBody secondMultipartBody = (MultipartBody) append;

        MultipartBody.Builder targetMultipartBodyBuilder = new MultipartBody.Builder();

        /* source */
        for (int x = 0; x < firstMultipartBody.size(); x++) {

            MultipartBody.Part part = firstMultipartBody.part(x);

            targetMultipartBodyBuilder.addPart(part);
        }

        /* append */
        for (int x = 0; x < secondMultipartBody.size(); x++) {

            MultipartBody.Part part = secondMultipartBody.part(x);

            targetMultipartBodyBuilder.addPart(part);
        }

        return targetMultipartBodyBuilder.build();
    }
}
