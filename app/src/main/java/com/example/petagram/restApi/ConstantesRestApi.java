package com.example.petagram.restApi;

public final class ConstantesRestApi {
    public static final String VERSION = "/v9.0/";
    public static final String ROOT_URL_IG = "https://graph.instagram.com/";
    public static final String ACCEES_TOKEN_IG = "IGQVJYanZA3bk9rN0hwcG1JTi1xUk1la3ltd1lHeWVuU2JBR25OQ1Q5Y0J2S0NUVGc2QlZAMZAXM4dzdRV242OERjYXgteHRjN0ZAfdldGbkNFSFBOc2FUWG5IYjZA0VnhtWllHc0ZARbm9ORExySnVFaVlLOQZDZD";
    public static final String KEY_ACCEES_TOKEN  = "?access_token=";
    public static final String USER_ID    = "17841444742300971";
    public static final String FIEDLS  = "username,media_count,media";//"&fields=username,media_count,media";
    public static final String FIEDLS_MEDIA  = "media_url";//"&fields=media_url";
    public static final String ID_MEDIA_DATA  = "{id_media}";
    public static final String USER  = "{user}";
    public static final String URL_GET_INFORMATION_USER  = USER;//+KEY_ACCEES_TOKEN+TOKEN+FIEDLS;
    public static final String URL_GET_INFORMATION_MEDIA_DATA =ID_MEDIA_DATA;//+KEY_ACCEES_TOKEN+TOKEN+FIEDLS_MEDIA;
    public static String Cuenta_Instagram  = "@petagrmapp";
    public static String gUserId  = USER_ID;
    public static String gToken  = ACCEES_TOKEN_IG;
    public static String gId_Dispositivo="" ;
    public static final String ROOT_URL_MI_API = "https://whispering-shore-83841.herokuapp.com/";
    public static final String KEY_POST_ID_TOKEN = "registrar-usuario/";
    public static final String KEY_POST_LIKE_FOTO = "registrar-like/";
    public static final String KEY_POST_NOTIFICAR_LIKE_FOTO = "notificar-like/{id}";
}
