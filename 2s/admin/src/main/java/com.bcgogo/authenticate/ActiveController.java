package com.bcgogo.authenticate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: MZDong
 * Date: 11-12-9
 * Time: 下午4:18
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/active.do")
public class ActiveController {

  @RequestMapping(params = "method=returntime")
  public void shopList(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
    try {
      Calendar now = Calendar.getInstance();
      PrintWriter out = response.getWriter();
      out.write("\"" + now.getTime() + "\"");
      out.close();
    } catch (Exception e) {
    }
  }

  //判断session是否存在
  @RequestMapping(params = "method=checksession")
  public void checkSession(HttpServletRequest request, HttpServletResponse response) {
    try {

      PrintWriter out = response.getWriter();
      if (request.getSession() == null
          || (request.getSession().getAttribute("shopId") == null && request.getSession().getAttribute("userId") == null)) {

        out.write("\"false\""); //不存在session
      } else {
        out.write("\"true\"");//存在session
      }
      out.close();

    } catch (Exception e) {
    }
  }


}