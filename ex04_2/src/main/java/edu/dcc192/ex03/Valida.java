package edu.dcc192.ex03;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Valida {
     @Autowired
    UsuarioRepository ur;
    public boolean testa(Usuario u){
        List<Usuario> lu = ur.findAll();
        boolean achou = false;
        for(Usuario i: lu){
            if(i.getLogin().equals(u.getLogin()) && i.getSenha().equals(u.getSenha())){
                achou=true;
                break;
            }
        }
        return achou;
    }
    public boolean existe(String login){
        List<Usuario> lu = ur.findAll();
        boolean achou = false;
        for(Usuario i: lu){
            if(i.getLogin().equals(login)){
                achou=true;
                break;
            }
        }
        return achou;
    }
}