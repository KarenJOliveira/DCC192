package edu.dcc192.ex03;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//import org.hibernate.mapping.List;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.springframework.ui.Model;


//import ch.qos.logback.core.model.Model;


@Controller
@SessionAttributes({ "usuario"})
public class HomeController {

    JSONArray records = new JSONArray();

    // Cria um atributo de modelo padrão para "usuario" se ele não existir na sessão
    @ModelAttribute("usuario")
    public Usuario criarUsuario() {
        return new Usuario();
    }

    @Autowired
    private GeradorSenha senha;

    @GetMapping("/")
    public ModelAndView home1(){ 
        ModelAndView mv = new ModelAndView();
        mv.setViewName("codigo");
        mv.addObject("senha", senha.GerarSenha());

        return mv;
    }

    // @RequestMapping("/login")
    // public String home(){
    //     return "login.html";
    // }

    @GetMapping("/login")
    public ModelAndView home2(@RequestParam String codigo, String captcha){
        ModelAndView mv = new ModelAndView();
        if(codigo.equals(captcha)){
            mv.setViewName("login");
        }else{
            mv.setViewName("codigo");
            mv.addObject("senha", senha.GerarSenha());
            mv.addObject("error", true);
        }
        return mv;
    }   

    
    @Autowired
    private Dados dados;
    
    @GetMapping("/index")
    public ModelAndView getMethodName(@ModelAttribute("usuario") Usuario usuario, String login) {
        ModelAndView mv = new ModelAndView();
        usuario.setLogin(login);
        boolean encontrou = false;
        for(int i=0;i<records.length();i++){
            String nome = records.getJSONArray(i).getString(0).toString();
            if(nome.equals(login)){
                int incr = records.getJSONArray(i).getInt(1) + 1;
                records.getJSONArray(i).put(1, incr);
                encontrou = true;
            }
        }
        if(!encontrou){
            records.put(new JSONArray().put(login).put(1));
        }
        
        mv.setViewName("index");
        mv.addObject("usuario", usuario); // Passa o nome como parâmetro para a página
        mv.addObject("dados", dados.pegaDados()); 
        return mv;
    }

    @GetMapping("/info")
    public ModelAndView getInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("info");
        return mv;
    }

    @GetMapping("/erroJava")
    public String erroJava()  throws Exception{
        throw new IOException("Erro no tratamento de dados") ;
    }

    @GetMapping("/return")
    public ModelAndView getMethodName(@ModelAttribute("usuario") Usuario usuario) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        mv.addObject("usuario", usuario); // Passa o nome como parâmetro para a página
        mv.addObject("dados", dados.pegaDados()); 
        return mv;
    }

    @GetMapping("logout")
    public ModelAndView getLogout() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login");
        mv.addObject("logout", true);
        return mv;
    }


    @Autowired
    UsuarioRepository ur;

    @GetMapping({"/users"})
    public ModelAndView requestMethodName() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("usuarios");
        List<Usuario> lu = ur.findAll();
        mv.addObject("usuarios",lu);
        return mv;
    }

    @GetMapping("/addUser")
    public ModelAndView getAddUser() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("adicionaUsuario");
        
        return mv;
    }
    
    @PostMapping("/saveUser")
    public ModelAndView saveUser(String login, String senha) {
        ur.save(new Usuario(login,senha));
        ModelAndView mv = new ModelAndView();
        mv.setViewName("usuarios");
        List<Usuario> lu = ur.findAll();
        mv.addObject("usuarios",lu);
        return mv;
    }


    

    {
        carrega();
    }

    public void carrega() {
        records.put(new JSONArray().put("Usuario").put("Acessos"));
        records.put(new JSONArray().put("Maria").put(10));
        records.put(new JSONArray().put("Joao").put(11));
        records.put(new JSONArray().put("Bia").put(6));
        records.put(new JSONArray().put("Lucas").put(10));
    }

    public String hoje(){
         // Obter a data do dia corrente e formatá-la como dia/mês
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        String formattedDate = currentDate.format(formatter);
        return formattedDate;

    }

    @GetMapping("/chart")
    public String getMethodLogin(Model model) {
        System.out.println(records.toString());
        model.addAttribute("jsonData", records.toString());
        model.addAttribute("diaMes", hoje());
        return "chart";
    }

}