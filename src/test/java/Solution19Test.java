import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * Solution19Test - 生命游戏算法测试类
 * 
 * 测试用例设计总体原则：
 * 1. 等价类划分原则：
 *    - 活细胞存活类：周围有2-3个活细胞的活细胞应继续存活
 *    - 活细胞死亡类：周围少于2个或超过3个活细胞的活细胞应死亡
 *    - 死细胞复活类：周围正好有3个活细胞的死细胞应复活
 *    - 死细胞依旧死亡类：周围不是正好3个活细胞的死细胞应保持死亡
 * 
 * 2. 边界值分析原则：
 *    - 测试空网格（全为死细胞）
 *    - 测试全活细胞网格
 *    - 测试单个细胞情况
 *    - 测试网格边界上的细胞
 * 
 * 3. 典型案例测试：
 *    - 静态模式（如方块）
 *    - 周期性模式（如闪烁器）
 *    - 典型示例输入
 */
@RunWith(JUnit4.class)
public class Solution19Test {

    private final Solution19 solution = new Solution19();

    /**
     * 测试目的：验证示例1的输入输出正确性
     * 测试用例：board = [[0,1,0],[0,0,1],[1,1,1],[0,0,0]]
     * 预期结果：[[0,0,0],[1,0,1],[0,1,1],[0,1,0]]
     */
    @Test
    public void testGameOfLifeExample1() {
        int[][] board = {
            {0, 1, 0},
            {0, 0, 1},
            {1, 1, 1},
            {0, 0, 0}
        };
        
        int[][] expected = {
            {0, 0, 0},
            {1, 0, 1},
            {0, 1, 1},
            {0, 1, 0}
        };
        
        solution.gameOfLife(board);
        assertTrue("示例1测试失败", arraysEqual(board, expected));
    }

    /**
     * 测试目的：验证示例2的输入输出正确性
     * 测试用例：board = [[1,1],[1,0]]
     * 预期结果：[[1,1],[1,1]]
     */
    @Test
    public void testGameOfLifeExample2() {
        int[][] board = {
            {1, 1},
            {1, 0}
        };
        
        int[][] expected = {
            {1, 1},
            {1, 1}
        };
        
        solution.gameOfLife(board);
        assertTrue("示例2测试失败", arraysEqual(board, expected));
    }

    /**
     * 测试目的：验证所有细胞都是死细胞的情况
     * 测试用例：3x3全为0的网格
     * 预期结果：所有细胞保持死亡状态
     */
    @Test
    public void testGameOfLifeAllDead() {
        int[][] board = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        
        int[][] expected = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        
        solution.gameOfLife(board);
        assertTrue("全死细胞测试失败", arraysEqual(board, expected));
    }

    /**
     * 测试目的：验证活细胞因孤独而死亡的情况（周围活细胞少于2个）
     * 测试用例：3x3网格，中心为活细胞，其余均为死细胞
     * 预期结果：中心活细胞死亡，其他保持不变
     */
    @Test
    public void testGameOfLifeUnderpopulation() {
        int[][] board = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        
        int[][] expected = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        
        solution.gameOfLife(board);
        assertTrue("孤独死亡测试失败", arraysEqual(board, expected));
    }

    /**
     * 测试目的：验证活细胞在理想环境下存活的情况（周围有2-3个活细胞）
     * 测试用例：2x2全为活细胞的网格
     * 预期结果：所有活细胞继续保持存活
     */
    @Test
    public void testGameOfLifeSurvival() {
        int[][] board = {
            {1, 1},
            {1, 1}
        };
        
        int[][] expected = {
            {1, 1},
            {1, 1}
        };
        
        solution.gameOfLife(board);
        assertTrue("理想存活测试失败", arraysEqual(board, expected));
    }

    /**
     * 测试目的：验证死细胞因繁殖而复活的情况（周围正好有3个活细胞）
     * 测试用例：3x3网格，死细胞被正好3个活细胞包围
     * 预期结果：中心死细胞复活
     */
    @Test
    public void testGameOfLifeReproduction() {
        int[][] board = {
            {0, 1, 0},
            {0, 0, 1},
            {0, 1, 0}
        };
        
        int[][] expected = {
            {0, 0, 0},
            {0, 1, 1},
            {0, 0, 0}
        };
        
        solution.gameOfLife(board);
        assertTrue("繁殖复活测试失败", arraysEqual(board, expected));
    }

    /**
     * 测试目的：验证活细胞因过度拥挤而死亡的情况（周围活细胞超过3个）
     * 测试用例：3x3全为活细胞的网格
     * 预期结果：角落的活细胞因有4个邻居而死亡，边缘的活细胞因有5个邻居而死亡
     */
    @Test
    public void testGameOfLifeOverpopulation() {
        int[][] board = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        
        int[][] expected = {
            {1, 0, 1},
            {0, 0, 0},
            {1, 0, 1}
        };
        
        solution.gameOfLife(board);
        assertTrue("过度拥挤测试失败", arraysEqual(board, expected));
    }

    /**
     * 测试目的：验证经典闪烁器模式的第一步转换
     * 测试用例：5x5网格，横向三个连续活细胞位于中央行
     * 预期结果：纵向三个连续活细胞位于中央列
     */
    @Test
    public void testGameOfLifeBlinkerPattern() {
        int[][] board = {
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
        };
        
        int[][] expected = {
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0}
        };
        
        solution.gameOfLife(board);
        assertTrue("闪烁器模式测试失败", arraysEqual(board, expected));
    }

    /**
     * 辅助方法：比较两个二维数组是否相等
     * 
     * @param a 第一个二维数组
     * @param b 第二个二维数组
     * @return 如果两个数组相等返回true，否则返回false
     */
    private boolean arraysEqual(int[][] a, int[][] b) {
        if (a.length != b.length) {
            return false;
        }
        
        for (int i = 0; i < a.length; i++) {
            if (a[i].length != b[i].length) {
                return false;
            }
            
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] != b[i][j]) {
                    return false;
                }
            }
        }
        
        return true;
    }
}