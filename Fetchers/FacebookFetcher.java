package Fetchers;

import facebook4j.*;
import facebook4j.auth.AccessToken;

/**
 * Created by parmj on 2/15/17.
 */
public class FacebookFetcher {
    public void getReactions(String id){
        Facebook facebook = new FacebookFactory().getInstance();
        String appId = "1091248824328225", appSecret = "xxxxxx";

        String commaSeparetedPermissions = "email,publish_stream";
        String accessToken = "xxxxx";
        facebook.setOAuthAppId(appId, appSecret);
        facebook.setOAuthPermissions(commaSeparetedPermissions);
        facebook.setOAuthAccessToken(new AccessToken(accessToken, null));

        try {
            ResponseList<Reaction> reactions = facebook.getPostReactions(id);

            for(Reaction r: reactions){
                System.out.println("REACTION............"+r.getType());
            }
        } catch (FacebookException e) {
            e.printStackTrace();
        }

    }

}
