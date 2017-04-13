package Fetchers;

import facebook4j.*;
import facebook4j.auth.AccessToken;

/**
 * Created by parmj on 2/15/17.
 */
public class FacebookFetcher {
    public void getReactions(String id){
        Facebook facebook = new FacebookFactory().getInstance();
        String appId = "1091248824328225", appSecret = "23f8176e7c61b61457fb51a04a2b58ef";

        String commaSeparetedPermissions = "email,publish_stream";
        String accessToken = "EAAPgfCqJICEBACOoteUnMeKDZCmQNszAumilHDt2ylKlfW2AHbfRZCXN8iDCZAd4MsKNMSBwzS0M5rowKmC6who7GxZCTZAuR7h6URSEdlEwmQ62tdeRWdiDS76mZA8ZCF61WS3OCfOL36l8erhEqhB1ZBlKJZAYoya4QAmV5jxHF69XQ2Cw9xDgkcTCuDoRS5JUZD";
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
