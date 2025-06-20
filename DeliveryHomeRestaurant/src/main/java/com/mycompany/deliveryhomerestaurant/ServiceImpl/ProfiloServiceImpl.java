/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.ServiceImpl;

import com.mycompany.deliveryhomerestaurant.Service.ProfiloService;
import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author franc
 */
import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
public class ProfiloServiceImpl implements ProfiloService{
    
    private EUtenteDAO utenteDAO;
    
    public ProfiloServiceImpl(EUtenteDAO utenteDAO){
        this.utenteDAO=utenteDAO;
    }
    
    @Override 
    public boolean Register(EUtente user){
         String email = user.getEmail();
        if (utenteDAO.findByUsername(email )!=null ){
            return false; //user is register
        } else {
            user.setPassword(hashPassword(user.getPassword()));
        utenteDAO.save(user);
        return true;
        }
    }
        
    @Override
    public boolean login(String username, String password) {
        EUtente user = utenteDAO.findByUsername(username);
        if (user != null && verifyPassword(password, user.getPassword())) {
            return true;
        }
        return false;
    }
    
    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
    
}
    
    