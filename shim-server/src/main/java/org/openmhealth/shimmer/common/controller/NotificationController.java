package org.openmhealth.shimmer.common.controller;

import org.openmhealth.shim.*;
import org.openmhealth.shim.common.mapper.ShimNotificationDataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static java.time.ZoneOffset.UTC;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * Created by Bharadwaj on 4/2/2017.
 */
@Configuration
@RestController
public class NotificationController {
    @Autowired
    private AccessParametersRepo accessParametersRepo;

    @Autowired
    private ShimRegistry shimRegistry;

    @RequestMapping(value = "/notification/{shim}/{action}",produces = APPLICATION_JSON_VALUE)
    public ShimDataResponse notification(
            @RequestParam(value = "username") String username,
            @PathVariable("action") String notificationAction,
            @RequestParam(value="scope") String scope,
            @PathVariable("shim") String shim,
            @RequestParam(value="callbackUrl",required = false) String callbackUrl,
            @RequestParam(value="comment",required = false) String comment
            ) throws ShimException
    {
        setPassThroughAuthentication(username,shim);
        ShimNotificationDataRequest shimNotificationDataRequest = new ShimNotificationDataRequest();

        shimNotificationDataRequest.setDataTypeKey(notificationAction);
        shimNotificationDataRequest.setCallbackUrl(callbackUrl);
        shimNotificationDataRequest.setComment(comment);
        shimNotificationDataRequest.setScope(scope);

        AccessParameters accessParameters = accessParametersRepo.findByUsernameAndShimKey(
                username, shim, new Sort(Sort.Direction.DESC, "dateCreated"));

        if (accessParameters == null) {
            throw new ShimException("User '" + username + "' has not authorized shim: '" + shim + "'");
        }
        shimNotificationDataRequest.setAccessParameters(accessParameters);

        return shimRegistry.getShim(shim).subscribe(shimNotificationDataRequest);
    }

    /**
     * Sets pass through authentication required by spring.
     */
    private void setPassThroughAuthentication(String username, String shim) {
        SecurityContextHolder.getContext().setAuthentication(new ShimAuthentication(username, shim));
    }

}
