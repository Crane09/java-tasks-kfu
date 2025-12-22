public class step_10 {
    private String printTextPerRole(String[] roles, String[] textLines) {
    // Prepare a StringBuilder for each role to collect their lines
    java.util.Map<String, StringBuilder> map = new java.util.HashMap<>();
    for (String role : roles) {
        map.put(role, new StringBuilder());
    }

    for (int i = 0; i < textLines.length; i++) {
        String line = textLines[i];
        int colon = line.indexOf(':');
        if (colon == -1) continue; // malformed line -- skip
        String roleName = line.substring(0, colon);
        String text;
        if (colon + 1 < line.length() && line.charAt(colon + 1) == ' ') {
            text = line.substring(colon + 2);
        } else {
            text = line.substring(colon + 1);
        }
        StringBuilder sb = map.get(roleName);
        if (sb != null) {
            sb.append(i + 1).append(") ").append(text).append('\n');
        }
    }

    StringBuilder result = new StringBuilder();
    for (int r = 0; r < roles.length; r++) {
        String role = roles[r];
        result.append(role).append(":\n");
        StringBuilder sb = map.get(role);
        if (sb != null && sb.length() > 0) {
            result.append(sb);
        }
        if (r != roles.length - 1) result.append('\n');
    }
    return result.toString();
}
}
