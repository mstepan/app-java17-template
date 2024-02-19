package com.max.app17.leetcode.hard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** <a href="https://leetcode.com/problems/robot-collisions/">2751. Robot Collisions.</a> */
public class RobotCollisions {

    public static void main(String[] args) throws Exception {

        int[] positions = {3, 5, 2, 6};
        int[] health = {10, 10, 15, 12};
        String directions = "RLRL";

        List<Integer> res =
                new RobotCollisions().survivedRobotsHealths(positions, health, directions);

        System.out.println(res);

        System.out.println("RobotCollisions done...");
    }

    static final class Robot {

        private static final Comparator<Robot> POSITION_ASC_CMP =
                Comparator.comparing(Robot::getPosition);

        final int position;
        int health;

        final Direction direction;

        Robot(int position, int health, Direction direction) {
            this.position = position;
            this.health = health;
            this.direction = direction;
        }

        public int getPosition() {
            return position;
        }
    }

    enum Direction {
        LEFT,
        RIGHT;
    }

    /** time: O(N*lgN) space: O(N) */
    public List<Integer> survivedRobotsHealths(int[] positions, int[] healths, String directions) {
        assert positions != null;
        assert healths != null;
        assert directions != null;
        assert positions.length == healths.length && healths.length == directions.length();

        Map<Integer, Robot> posToRobot = createPositionsToRobot(positions, healths, directions);

        List<Robot> allRobots = new ArrayList<>(posToRobot.values());

        allRobots.sort(Robot.POSITION_ASC_CMP);

        Deque<Robot> stack = new ArrayDeque<>();

        for (Robot cur : allRobots) {
            if (stack.isEmpty() || cur.direction == Direction.RIGHT) {
                stack.push(cur);
            } else if (cur.direction == Direction.LEFT) {

                while (true) {
                    if (stack.isEmpty()) {
                        stack.push(cur);
                        break;
                    }
                    Robot last = stack.pop();

                    if (last.direction == Direction.LEFT) {
                        stack.push(last);
                        stack.push(cur);
                        break;
                    } else { // last.direction == RIGHT
                        if (last.health == cur.health) {
                            last.health = 0;
                            cur.health = 0;
                            break;
                        } else if (last.health > cur.health) {
                            last.health -= 1;
                            cur.health = 0;
                            stack.push(last);
                            break;
                        } else {
                            last.health = 0;
                            cur.health -= 1;
                        }
                    }
                }
            }
        }

        return constructResult(positions, posToRobot);
    }

    private List<Integer> constructResult(int[] positions, Map<Integer, Robot> posToRobot) {

        List<Integer> healthRes = new ArrayList<>();

        for (int curPos : positions) {
            Robot robot = posToRobot.get(curPos);
            if (robot.health > 0) {
                healthRes.add(robot.health);
            }
        }

        return healthRes;
    }

    private Map<Integer, Robot> createPositionsToRobot(
            int[] positions, int[] healths, String directions) {

        Map<Integer, Robot> res = new HashMap<>();

        for (int i = 0; i < positions.length; ++i) {

            int pos = positions[i];
            int health = healths[i];
            Direction dir = directions.charAt(i) == 'L' ? Direction.LEFT : Direction.RIGHT;

            res.put(pos, new Robot(pos, health, dir));
        }

        return res;
    }
}
