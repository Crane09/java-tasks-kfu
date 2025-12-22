public class step_7 {
    public static void moveRobot(RobotConnectionManager robotConnectionManager,
                             int toX, int toY) {

    for (int attempt = 1; attempt <= 3; attempt++) {
        RobotConnection connection = null;
        try {
            connection = robotConnectionManager.getConnection();
            connection.moveRobotTo(toX, toY);
            return;
        } catch (RobotConnectionException e) {
            if (attempt == 3) {
                throw e;
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignored) {

                }
            }
        }
    }
}
}
