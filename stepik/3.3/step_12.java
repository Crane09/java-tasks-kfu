public class step_12 {
    public static void moveRobot(Robot robot, int toX, int toY) {
        int dx = toX - robot.getX();
        if (dx != 0) {
            Direction needed = dx > 0 ? Direction.RIGHT : Direction.LEFT;
            rotateTo(robot, needed);
            int steps = Math.abs(dx);
            for (int i = 0; i < steps; i++) robot.stepForward();
        }

        int dy = toY - robot.getY();
        if (dy != 0) {
            Direction needed = dy > 0 ? Direction.UP : Direction.DOWN;
            rotateTo(robot, needed);
            int steps = Math.abs(dy);
            for (int i = 0; i < steps; i++) robot.stepForward();
        }
    }

    private static void rotateTo(Robot robot, Direction target) {
        while (robot.getDirection() != target) {
            robot.turnRight();
        }
    }
}
