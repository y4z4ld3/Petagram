package com.example.petagram.restApi.model;

public class LikeUsuarioRespose extends  UsuarioResponse{
    protected String id_foto_firebase;

    public LikeUsuarioRespose(String id, String id_dispositivo, String id_usuario_instagram, String id_foto_firebase) {
        super(id, id_dispositivo, id_usuario_instagram);
        this.id_foto_firebase = id_foto_firebase;
    }

    public LikeUsuarioRespose(String id_foto_firebase) {
        this.id_foto_firebase = id_foto_firebase;
    }

    public String getId_foto_firebase() {
        return id_foto_firebase;
    }

    public void setId_foto_firebase(String id_foto_firebase) {
        this.id_foto_firebase = id_foto_firebase;
    }
}
