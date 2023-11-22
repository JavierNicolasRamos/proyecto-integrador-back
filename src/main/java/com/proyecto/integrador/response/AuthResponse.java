package com.proyecto.integrador.response;

import com.proyecto.integrador.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String jwt;
    private Role role;
    private String email;

    public String toStringIndented(int indentLevel) {
        StringBuilder indent = new StringBuilder();
        indent.append("  ".repeat(Math.max(0, indentLevel)));

        return "{" +
                "\n" + indent + "  jwt=" + this.jwt +
                "\n" + indent + "  rol=" + this.role +
                "\n" + indent + "  email=" + this.email +
                "\n" + indent + "}";
    }
}
