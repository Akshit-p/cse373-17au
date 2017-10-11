package calculator.ast;

import calculator.interpreter.Environment;
import calculator.errors.EvaluationError;
import calculator.gui.ImageDrawer;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import misc.exceptions.NotYetImplementedException;

/**
 * All of the static methods in this class are given the exact same parameters for consistency. You can often ignore
 * some of these parameters when implementing your methods.
 *
 * Some of these methods should be recursive. You may want to consider using public-private pairs in some cases.
 * 
 * @author Akshit Patel
 * @author Hoabo Zhang
 */
public class ExpressionManipulators {
    /**
     * Takes the given AstNode node and attempts to convert it into a double.
     *
     * Returns a number AstNode containing the computed double.
     *
     * @throws EvaluationError
     *             if any of the expressions contains an undefined variable.
     * @throws EvaluationError
     *             if any of the expressions uses an unknown operation.
     */
    public static AstNode toDouble(Environment env, AstNode node) {
        // To help you get started, we've implemented this method for you.
        // You should fill in the TODOs in the 'simplifyHelper' method.
        return new AstNode(toDoubleHelper(env.getVariables(), node));
    }

    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
        // There are three types of nodes, so we have three cases.
        if (node.isNumber()) {
            return node.getNumericValue();
        } else if (node.isVariable()) {
            if (!variables.containsKey(node.getName())) {
                // If the expression contains an undefined variable, we give up.
                throw new EvaluationError("Undefined variable: " + node.getName());
            }
            return toDoubleHelper(variables, variables.get(node.getName()));
        } else {
            String name = node.getName();
            IList<AstNode> children = node.getChildren();

            // this section of code assumes the data provided
            // is properly formed or else will result
            // in IndexOutOfBoundsException

            if (name.equals("toDouble")) {
                return toDoubleHelper(variables, children.get(0));
            }

            if (name.equals("+")) {
                return toDoubleHelper(variables, children.get(0)) + toDoubleHelper(variables, children.get(1));
            } else if (name.equals("-")) {
                return toDoubleHelper(variables, children.get(0)) - toDoubleHelper(variables, children.get(1));
            } else if (name.equals("*")) {
                return toDoubleHelper(variables, children.get(0)) * toDoubleHelper(variables, children.get(1));
            } else if (name.equals("/")) {
                return toDoubleHelper(variables, children.get(0)) / toDoubleHelper(variables, children.get(1));
            } else if (name.equals("^")) {
                return Math.pow(toDoubleHelper(variables, children.get(0)), toDoubleHelper(variables, children.get(1)));
            } else if (name.equals("negate")) {
                return -toDoubleHelper(variables, children.get(0));
            } else if (name.equals("sin")) {
                return Math.sin(toDoubleHelper(variables, children.get(0)));
            } else if (name.equals("cos")) {
                return Math.cos(toDoubleHelper(variables, children.get(0)));
            } else {
                throw new EvaluationError("Unknown operation: " + name);
            }
        }
    }

    public static AstNode simplify(Environment env, AstNode node) {
        // Try writing this one on your own!
        // Hint 1: Your code will likely be structured roughly similarly
        // to your "toDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        // to call your "toDouble" method in some way

        return simplifyHelper(env.getVariables(), node);
    }

    private static AstNode simplifyHelper(IDictionary<String, AstNode> variables, AstNode node) {
        if (node.isNumber()) {
            return node;
        } else if (node.isVariable()) {
            if (!variables.containsKey(node.getName())) {
                return node;
            }
            return simplifyHelper(variables, variables.get(node.getName()));
        } else {
            String name = node.getName();
            IList<AstNode> children = node.getChildren();

            // this section of code assumes the data provided
            // is properly formed or else will result
            // in IndexOutOfBoundsException

            if (name.equals("simplify")) {
                return simplifyHelper(variables, children.get(0));
            } else if (name.equals("+") || name.equals("-") || name.equals("*") || name.equals("^")
                    || name.equals("/")) {
                // simplify and evaluate the branches.
                AstNode left = simplifyHelper(variables, children.get(0));
                AstNode right = simplifyHelper(variables, children.get(1));
                // compute exact figure for all operators except division.
                if (left.isNumber() && right.isNumber() && !name.equals("/")) {
                    return new AstNode(toDoubleHelper(variables, node));
                } else {
                    IList<AstNode> temp = new DoubleLinkedList<>();
                    temp.add(left);
                    temp.add(right);
                    return new AstNode(name, temp);
                }
            } else {
                // case when name.equals("negate") || name.equals("sin") || name.equals("cos")

                AstNode eval = simplifyHelper(variables, children.get(0));
                IList<AstNode> temp = new DoubleLinkedList<>();
                if (name.equals("negate") && eval.isNumber()) {
                    return new AstNode(-eval.getNumericValue());
                }
                temp.add(eval);
                return new AstNode(name, temp);
            }
        }
    }

    /**
     * Expected signature of plot:
     *
     * >>> plot(exprToPlot, var, varMin, varMax, step)
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This command will plot the equation "3 * x", varying "x" from 2 to 5 in 0.5 increments. In this case, this means
     * you'll be plotting the following points:k
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4" from -10 to 10 in 0.01 increments. In
     * this case, "a" is our "x" variable.
     *
     * >>> c := 4 4 >>> step := 0.01 0.01 >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * ---
     *
     * @throws EvaluationError
     *             if any of the expressions contains an undefined variable.
     * @throws EvaluationError
     *             if varMin > varMax
     * @throws EvaluationError
     *             if 'var' was already defined
     * @throws EvaluationError
     *             if 'step' is zero or negative
     */
    public static AstNode plot(Environment env, AstNode node) {
        IDictionary<String, AstNode> variables = env.getVariables();
        IList<AstNode> args = node.getChildren();

        String var = args.get(1).getName();
        // simplify the expressions in case variables were given.
        AstNode varMin = simplifyHelper(variables, args.get(2));
        AstNode varMax = simplifyHelper(variables, args.get(3));
        AstNode step = simplifyHelper(variables, args.get(4));

        if (!varMin.isNumber() || !varMax.isNumber() || !step.isNumber()) {
            throw new EvaluationError("undefined parameters given");
        }
        if (variables.containsKey(var)) {
            throw new EvaluationError("var is already defined");
        }

        double min = varMin.getNumericValue();
        double max = varMax.getNumericValue();
        double inc = step.getNumericValue();

        if (min > max) {
            throw new EvaluationError("varMin > varMax");
        }
        if (inc <= 0) {
            throw new EvaluationError("step is zero or negative");
        }

        IList<Double> yVal = new DoubleLinkedList<>();
        IList<Double> xVal = new DoubleLinkedList<>();
        AstNode expr = args.get(0);
        for (double i = min; i <= max; i += inc) {
            variables.put(var, new AstNode(i));
            double y = toDoubleHelper(variables, expr);
            yVal.add(y);
            xVal.add(i);
        }
        variables.remove(var); // clean up the x-value from the environment.
        ImageDrawer drawer = env.getImageDrawer();
        drawer.drawScatterPlot(var + " vs f(" + var + ")", var, "f(" + var + ")" , xVal, yVal);

        // Note: every single function we add MUST return an
        // AST node that your "simplify" function is capable of handling.
        // However, your "simplify" function doesn't really know what to do
        // with "plot" functions (and what is the "plot" function supposed to
        // evaluate to anyways?) so we'll settle for just returning an
        // arbitrary number.
        //
        // When working on this method, you should uncomment the following line:
        //
        return new AstNode(1);
    }
}
