package org.openmhealth.shim.common.mapper;

import org.openmhealth.shim.ShimDataRequest;

/**
 * Created by Bharadwaj on 4/2/2017.
 */
public class ShimNotificationDataRequest extends ShimDataRequest {

    private String callbackUrl;

    private String comment;

    private String scope;

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
