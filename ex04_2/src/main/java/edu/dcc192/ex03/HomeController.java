package edu.dcc192.ex03;

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

//import ch.qos.logback.core.model.Model;


@Controller
@SessionAttributes({ "usuario"})
public class HomeController {

    // Cria um atributo de modelo padrão para "usuario" se ele não existir na sessão
    @ModelAttribute("usuario")
    public Usuario criarUsuario() {
        return new Usuario();
    }

    @Autowired
    private GeradorSenha capt;
    

    // @RequestMapping("/login")
    // public String home(){
    //     return "login.html";
    // }

    // @GetMapping("/login")
    // public ModelAndView home2(@RequestParam String codigo, String captcha){
    //     ModelAndView mv = new ModelAndView();
    //     if(codigo.equals(captcha)){
    //         mv.setViewName("login");
    //     }else{
    //         mv.setViewName("codigo");
    //         mv.addObject("senha", senha.GerarSenha());
    //         mv.addObject("error", true);
    //     }
    //     return mv;
    // }   

    
    @Autowired
    private Dados dados;
    
    @GetMapping("/index")
    public ModelAndView getMethodName(@RequestParam String codigo, String captcha, @ModelAttribute("usuario") Usuario usuario) {
        ModelAndView mv = new ModelAndView();
        
        if(codigo.equals(captcha)){
            mv.setViewName("index");
            mv.addObject("usuario", usuario); // Passa o nome como parâmetro para a página
            mv.addObject("dados", dados.pegaDados());
        }else{
            mv.setViewName("codigo");
            mv.addObject("senha", capt.GerarSenha());
            mv.addObject("error", true);
        } 
        return mv;
    }

    @GetMapping("/info")
    public ModelAndView getInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("info");
        return mv;
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
        mv.setViewName("loginInicial");
        mv.addObject("logout", true);
        return mv;
    }


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

    @GetMapping("/")
    public ModelAndView getLogin() {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("loginInicial");

        return mv;
    }

    

    @PostMapping("/validaLogin")
    public ModelAndView validaLogin(@ModelAttribute("usuario") Usuario usuario, @RequestParam String login, String password) {
        ModelAndView mv = new ModelAndView();

        if(existe(login)){
            usuario = new Usuario(login,password);
            if (testa(usuario)) {
                mv.setViewName("codigo");
                mv.addObject("senha", capt.GerarSenha());
            }else{
                mv.setViewName("loginInicial");
                mv.addObject("erro", true);
            }
        }else{
            mv.setViewName("adicionaUsuario");
        }
        
        return mv;
    }

    @GetMapping("/captcha")
    public ModelAndView home1(){ 
        ModelAndView mv = new ModelAndView();
        mv.setViewName("codigo");
        mv.addObject("senha", capt.GerarSenha());
        return mv;
    }

    @GetMapping({"/users"})
    public ModelAndView requestMethodName() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("usuarios");
        List<Usuario> lu = ur.findAll();
        mv.addObject("usuarios",lu);
        return mv;
    }

    //VERIFICAR SE CADASTRA DUAS VEZES
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
        // mv.setViewName("usuarios");
        // List<Usuario> lu = ur.findAll();
        // mv.addObject("usuarios",lu);
        mv.setViewName("loginInicial");
        return mv;
    }

}