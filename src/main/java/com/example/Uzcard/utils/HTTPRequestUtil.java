package com.example.Uzcard.utils;


import com.example.Uzcard.dto.JWTDTO;
import com.example.Uzcard.enums.ProfileRole;
import com.example.Uzcard.exp.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;

public class HTTPRequestUtil {
    public static Integer getProfileId(HttpServletRequest request, ProfileRole... requiredRoleList) {
        Integer id = (Integer) request.getAttribute("id");
        ProfileRole role = (ProfileRole) request.getAttribute("role");
        if (requiredRoleList.length==0)return id;
        for (ProfileRole requiredRole : requiredRoleList) {
            if (role.equals(requiredRole)) {
                return id;
            }
        }
        throw new ForbiddenException("Method not allowed");

    }

    public static JWTDTO getJWTDTO(HttpServletRequest request, ProfileRole... requiredRoleList) {
        Integer id = (Integer) request.getAttribute("id");
        ProfileRole role = (ProfileRole) request.getAttribute("role");
        JWTDTO dto = new JWTDTO(id,role);

        for (ProfileRole requiredRole : requiredRoleList) {
            if (role.equals(requiredRole)) {
                return dto;
            }
        }
        throw new ForbiddenException("Method not allowed");
    }


}
