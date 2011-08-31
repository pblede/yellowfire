/* 
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package za.co.yellowfire.ui.security;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * deprecated Using LoginController login which authenticates using JAAS via the servler container
 */
@Named
@SessionScoped
public class Identity implements Serializable
{
   private boolean loggedIn;

   //@Inject
   //private FacesContext facesContext;

   @Inject
   private Authenticator authenticator;

   @Inject
   private Credentials credentials;

   public String getUsername()
   {
      return credentials.getUsername();
   }

   public boolean isLoggedIn()
   {
      return loggedIn;
   }

   public void login()
   {
      if (authenticator.authenticate())
      {
         loggedIn = true;
         credentials.setPassword(null);
      }
   }

   public String logout()
   {
      loggedIn = false;
      //HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
      //session.invalidate();
      return "home?faces-redirect=true";
   }

}
