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

import za.co.yellowfire.domain.profile.Authenticated;
import za.co.yellowfire.domain.profile.Credential;
import za.co.yellowfire.domain.profile.User;
import za.co.yellowfire.domain.profile.UserManager;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

//import org.jboss.seam.international.status.Messages;
//import za.co.yellowfire.ui.i18n.DefaultBundleKey;

/**
 * This implementation of <strong>Authenticator</strong> cross references the
 * values of the user's credentials against the database.
 * 
 * @author Dan Allen
 * deprecated Using LoginController login which authenticates using JAAS via the servler container
 */
@Stateless
public class Authenticator {
   //@Inject
   //private Instance<Logger> log;

   @EJB
   private UserManager userManager;
   
   //@Inject
   //private Messages messages;

   @Inject
   private Credentials credentials;

   @Inject
   @Authenticated
   private Event<User> loginEventSrc;

    /**
     * Authenticates the user
     * @return Whether the user authentication succeeded
     */
   public boolean authenticate()
   {
      //log.get().info("Logging in " + credentials.getUsername());
      if ((credentials.getUsername() == null) || (credentials.getPassword() == null))
      {
         //messages.info(new DefaultBundleKey("identity_loginFailed"));
         return false;
      }

      User u = new User();
      u.setName(credentials.getUsername());
      u.setPassword(credentials.getPassword());
      u.setPasswordConfirmation(credentials.getPassword());
      User user = userManager.login(new Credential(u.getName(), u.getPassword()));
      
      //User user = em.find(User.class, credentials.getUsername());
      if ((user != null) && user.getPassword().equals(credentials.getPassword()))
      {
         loginEventSrc.fire(user);
         //messages.info(new DefaultBundleKey("identity_loggedIn"), user.getName());
         return true;
      }
      else
      {
         //messages.info(new DefaultBundleKey("identity_loginFailed"));
         return false;
      }
   }

}
