package com.traits.struts.hello;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by yefeng on 14-3-27.
 */
public class HelloAction extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();

    public String execute() {
        request.setAttribute("hello", "hello world!");
        return SUCCESS;
    }
}