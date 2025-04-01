package com.example.ATMApplication.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ATMApplication.entity.ATM;
import com.example.ATMApplication.entity.TransferRespo;
import com.example.ATMApplication.service.ATMService;
@Controller
public class ATMController {
	private final ATMService service;

	public ATMController(ATMService service) {
		super();
		this.service = service;
	}
	 @PostMapping("/Register")
	    public String addRegist(ATM user) {
	    	service.addUser(user);
	    	return "Registerationsuccessful.jsp";
	       
	    }
	    @PostMapping("/Login")
	    public String login(@RequestParam("cardNumbers") long cardNumber,@RequestParam("pins") int pin,Model model)
	    {
	      boolean isvalid = service.displayUser(cardNumber,pin);
	        String[] cards= service.fetchcard(cardNumber);
	         
			if(isvalid)
			{
				model.addAttribute("ATM",cards);
				return "Welcome.jsp";
			}
			else
			{
				return "login.jsp";
			}
			
			 
	    }
	   
	    @PostMapping("/Deposit")
	  
	    	public String deposit(@RequestParam int amount,@RequestParam long cardNumber)
	    	{
	    		service.addAmount(amount,cardNumber);
	    		return "Successfullydeposited.jsp";
	    	}
	    @GetMapping("/balance")
	    public String balance(@RequestParam long cardNumber,@RequestParam int pin,Model model)
	    {
	       List<ATM> user=service.balanceAmount(cardNumber,pin);
	       model.addAttribute("Details",user);
	       return "balance.jsp";
	    }
	  /*  public String balance(
	    	    @RequestParam long cardNumber,
	    	    @RequestParam int pin,
	    	    Model model) {
	    	    
	    	    RegistUser user = service.getUserByCredentials(cardNumber, pin);
	    	    
	    	    if(user == null) {
	    	        model.addAttribute("error", "Invalid card number or PIN");
	    	        return "error.jsp";  // or redirect to login page
	    	    }
	    	    
	    	    model.addAttribute("Details", user);
	    	    return "balance.jsp";
	    	}*/
	    @PostMapping("/Withdraw")
	    public String withdrawn(@RequestParam long cardNumber,@RequestParam int pin,@RequestParam int amount)
	    {
	          boolean c	=service.withdraw(cardNumber,pin,amount);
	    	if(c)
	    	{
	    		return "successfully withdrawn.jsp";
	    	}
	    	else
	    	{
	    		return "insufficent balance.jsp";
	    	}
	    	
	    }
	  /*  @PostMapping("/Transfer")
	    public String transfer(@RequestParam("toAccount") long cardNumber,@RequestParam("fromAccount") long cardNumberf,@RequestParam("amount") int amount,@RequestParam("pin") int pin)
	    {
	    	service.transfering(cardNumber,cardNumbers,amount,pin);
	    	return "";
	    }
	  */
	  /*  @PostMapping("/transfer")
	    public String transfer(
	            @RequestParam("toAccount") long toAccount,
	            @RequestParam("fromAccount") long fromAccount,
	            @RequestParam("amount") int amount,
	            @RequestParam("pin") int pin,Model model) {
	    	Map<String, Object> response=service.trans(toAccount,fromAccount,amount,pin);
	    	model.addAllAttributes(response);
	       return "transfer.jsp";
	    }*/
	    @PostMapping("/transfer")
	    public String transfer(
	            @RequestParam("toAccount") long toAccount,
	            @RequestParam("fromAccount") long fromAccount,
	            @RequestParam("amount") int amount,
	            @RequestParam("pin") int pin, Model model) {
	        Map<String, Object> response = service.trans(toAccount, fromAccount, amount, pin);
	        
	        // Add all response attributes to the model
	        model.addAllAttributes(response);
	        
	        // Add specific attributes that your JSP expects
	        if (response.containsKey("valid")) {
	            model.addAttribute("transferResponse", new TransferRespo(
	                "Success",
	                "Transfer completed successfully",
	                fromAccount,
	                toAccount,
	                amount,
	                (Integer) response.get("newamount")
	            ));
	        } else {
	            model.addAttribute("transferResponse", new TransferRespo(
	                "Failed",
	                "Invalid account details or PIN",
	                fromAccount,
	                toAccount,
	                0,
	                0
	            ));
	        }
	        
	        return "transfer.jsp";
	    }
	    @PostMapping("/ChangePin")
	    public String pin(@RequestParam("cardNumber") long cardNumber,@RequestParam("currentPin")int currentPin,@RequestParam("newPin")int newPin,@RequestParam("confirmPin")int confirmPin)
	    {
	      boolean valid	=service.changePin(cardNumber,currentPin,newPin,confirmPin);
	      if(valid)
	    	return"successfullypin.jsp";
	      else
	    	  return"pinNotchanged.jsp";
	    }
	    @PostMapping("/ProcessForgotPin")
	    public String forgotPin(@RequestParam("cardNumber") long cardNumber,
	                           @RequestParam("email") String email,
	                           @RequestParam("mobile") long phoneNumber,
	                           Model model) {
	        Map<String, Object> response = service.forgot(cardNumber, email, phoneNumber);
	        model.addAttribute("pin", response.get("pin"));
	        return "successfulpin.jsp"; // or redirect as needed
	    }
	   
		
		

}
