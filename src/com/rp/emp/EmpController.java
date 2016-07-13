package com.rp.emp;
import java.io.IOException;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

//import com.rp.DBUtil;
import com.rp.LogUtil;


//@WebServlet("/ControllerServlet")
public class EmpController extends HttpServlet {
	final static Logger logger = Logger.getLogger(EmpController.class);
    private static final long serialVersionUID = 1L;       

    public EmpController() {
        super();
    
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        doPost(request, response);  
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
         
		 response.setCharacterEncoding("UTF-8");
		 response.setContentType("text/html; charset=UTF-8");
		 
		 PrintWriter out = response.getWriter();
		 
        HttpSession session = request.getSession();
        
        EmpSearchDto sdto = new EmpSearchDto();
        sdto.setSearch_type(request.getParameter("search_type"));
        sdto.setSearch_string(request.getParameter("search_string"));
        
   	     EmpDto dto = new EmpDto();
   	     EmpSvc svc = new EmpSvc();
       
        //RequestURI 구하기
        String command = request.getRequestURI();        // /jdbc-sample-mvc/login/login.do
        if (command.indexOf(request.getContextPath()) == 0) {       
            command = command.substring(request.getContextPath().length()); // /hello.do
        }
        System.out.println("command = " + command + " : ");
        
        int rt =0;        
     
        logger.info("controller start ==============================================="); 

        
        if (session.getAttribute("id") == null &&  !("/login_emp.do").equals(command) )
        {
        	RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
        	logger.info("#########  senssion null!!!!!!!"); 
            
        	rd.forward(request, response);
        //	response.sendRedirect("select_emp_list.do");
        } else {     
        	logger.info("#########  senssion not null!!!!!!!"); 

        if (("/select_emp_list.do").equals(command)) {
        	logger.info("controller : select_emp_list.do ==============================================="); 
        	
             request.setAttribute("list",(ArrayList<EmpDto>)svc.getEmpList(sdto));
            
             RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/jsp/emp_select_list.jsp");
            
             rd.forward(request, response);
            
        } else if (("/select_emp_detail.do").equals(command)) {
        	logger.info("controller : select_emp_detail.do ===============================================");

        	  dto.setSeq(Integer.parseInt(request.getParameter("seq")));

            request.setAttribute("detail",(EmpDto)svc.selectDetail(dto));    	   
            RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/jsp/emp_select_detail.jsp");
           
            rd.forward(request, response);
            
         } else if (("/insert_emp_form.do").equals(command)) {
        	 logger.info("controller : insert_emp_form.do ===============================================");
        	 
           RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/jsp/emp_insert_form.jsp");
           rd.forward(request, response);
        
        } else if (("/insert_emp.do").equals(command)) {
        	logger.info("controller : insert_emp.do ===============================================");
        	 
        	 //dto.setSeq(Integer.parseInt(request.getParameter("seq")));
        	 dto.setId(Integer.parseInt(request.getParameter("id")));
        	 dto.setAge(Integer.parseInt(request.getParameter("age")));
        	 dto.setFirst(request.getParameter("first"));
        	 dto.setLast(request.getParameter("last"));
        	 dto.setPasswd(request.getParameter("passwd"));
        	 dto.setDeptSeq(Integer.parseInt(request.getParameter("dept_seq")));      
        	 
        	 logger.info("controller : insert_emp.do : EmpDto Info " + dto.toString());
        	 
        	 rt = svc.addEmp(dto);
        	 
        	 if ( rt > 0){
        		 logger.info("controller : insert_emp.do : Success !! ===============================================");
        		 response.sendRedirect("select_emp_list.do");
        		 
        	 } 	else {       		 
        		// PrintWriter out = response.getWriter();
        		 logger.info("controller : insert_emp.do : Fail !! ===============================================");
        		 
        		 out.println("<script language='javascript'>");
        		 out.println("alert('정상적으로 처리 되지 않았습니다.');");
        		 //out.println("document.location.href='emplist.do'");
        		 out.println("history.back();");
        		 out.println("</script>");
        	 }
         } else if (("/update_emp_form.do").equals(command)) {
        	 logger.info("controller : update_emp_form.do ===============================================");

        	 dto.setSeq(Integer.parseInt(request.getParameter("seq")));
      	     
        	 request.setAttribute("detail",(EmpDto)svc.selectDetail(dto)); 
      	     RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/jsp/emp_update_form.jsp");
            
      	     rd.forward(request, response);
           //response.sendRedirect("emp.do");
        } else if (("/update_emp.do").equals(command)) {
        	 logger.info("controller : update_emp.do ===============================================");

       	 dto.setSeq(Integer.parseInt(request.getParameter("seq")));
       	 dto.setId(Integer.parseInt(request.getParameter("id")));
       	 dto.setAge(Integer.parseInt(request.getParameter("age")));
       	 dto.setFirst(request.getParameter("first"));
       	 dto.setPasswd(request.getParameter("passwd"));
       	 dto.setLast(request.getParameter("last"));
       	 dto.setDeptSeq(Integer.parseInt(request.getParameter("dept_seq"))); 
            
            rt = svc.EmpUpdate(dto);
     	     
          	 if ( rt > 0){
          		logger.info("controller : update_emp.do : Success ~!!!! ===============================================");
        		 RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/jsp/emp_select_list.jsp");
        		 
        		 response.sendRedirect("select_emp_list.do");
          	 } 	else {
          		logger.info("controller : update_emp.do : Fail ~!!!! ===============================================");
        		 //PrintWriter out = response.getWriter();
        		 
        		 out.println("<script language='javascript'>");
        		 out.println("alert('정상적으로 처리 되지 않았습니다.');");
        		 out.println("history.back();");
        		 out.println("</script>");
        	 } 	
        } else if (("/delete_emp.do").equals(command)) {  
        	logger.info("controller : delete_emp.do ===============================================");

          	 dto.setSeq(Integer.parseInt(request.getParameter("seq")));
        	
            rt = svc.EmpDelete(dto);

       	 if ( rt > 0){
    		 //request.setAttribute("list",(ArrayList<EmpDto>)svc.getEmpList(request));
    		// RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/src/emp_select_list.jsp");
    		 
    		 response.sendRedirect("select_emp_list.do");
       	     }
    	 } else if (("/login_emp.do").equals(command)) {       	 

        	 dto.setId(Integer.parseInt(request.getParameter("id")));
         	 dto.setPasswd(request.getParameter("passwd"));
         	 
     		logger.info("controller : Login_emp.do =======================================");
     		logger.info(dto.toString());
     		
        	 rt = svc.EmpLogin(dto);
        	 
            System.out.println("rt = " + rt);   
        		
        		if ( rt == 1 ){
        			//HttpSession session = request.getSession();
					session.setAttribute("id",dto.getId());

        		    RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/jsp/emp_select_list.jsp");
          		    response.sendRedirect("select_emp_list.do");
        		        
        	    } 	 else
        	       {
                  logger.info("rt : " + rt);
                  
         		    out.println("<script language='javascript'>");
         		    out.println("alert('아이디 또는 패스워드가 틀립니다..');");
         		    out.println("history.back();");
         		    out.println("</script>");
        	    	 
        	       }
         }  else if (("/logout_emp.do").equals(command)) 
                   {
        	         session.invalidate();
        	        logger.info("Log OUT !!!!! ");
        	         
          		    out.println("<script language='javascript'>");
          		    out.println("alert('정상적으로 로그아웃 처리 되었습니다.');");
          		    out.println("document.location.href='select_emp_list.do'");
          		    out.println("</script>");                 
                    } 
        	 
         }
       }
} 

