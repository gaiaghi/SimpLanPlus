// Generated from SimpLanPlus.g4 by ANTLR 4.9.3
package parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SimpLanPlusParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SimpLanPlusVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SimpLanPlusParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(SimpLanPlusParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignmentL}
	 * labeled alternative in {@link SimpLanPlusParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentL(SimpLanPlusParser.AssignmentLContext ctx);
	/**
	 * Visit a parse tree produced by the {@code deletionL}
	 * labeled alternative in {@link SimpLanPlusParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeletionL(SimpLanPlusParser.DeletionLContext ctx);
	/**
	 * Visit a parse tree produced by the {@code printL}
	 * labeled alternative in {@link SimpLanPlusParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintL(SimpLanPlusParser.PrintLContext ctx);
	/**
	 * Visit a parse tree produced by the {@code retL}
	 * labeled alternative in {@link SimpLanPlusParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetL(SimpLanPlusParser.RetLContext ctx);
	/**
	 * Visit a parse tree produced by the {@code iteL}
	 * labeled alternative in {@link SimpLanPlusParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIteL(SimpLanPlusParser.IteLContext ctx);
	/**
	 * Visit a parse tree produced by the {@code callL}
	 * labeled alternative in {@link SimpLanPlusParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallL(SimpLanPlusParser.CallLContext ctx);
	/**
	 * Visit a parse tree produced by the {@code blockL}
	 * labeled alternative in {@link SimpLanPlusParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockL(SimpLanPlusParser.BlockLContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decFunL}
	 * labeled alternative in {@link SimpLanPlusParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecFunL(SimpLanPlusParser.DecFunLContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decVarL}
	 * labeled alternative in {@link SimpLanPlusParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecVarL(SimpLanPlusParser.DecVarLContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpLanPlusParser#decFun}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecFun(SimpLanPlusParser.DecFunContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpLanPlusParser#decVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecVar(SimpLanPlusParser.DecVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpLanPlusParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(SimpLanPlusParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpLanPlusParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg(SimpLanPlusParser.ArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpLanPlusParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(SimpLanPlusParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpLanPlusParser#lhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLhs(SimpLanPlusParser.LhsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpLanPlusParser#deletion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeletion(SimpLanPlusParser.DeletionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpLanPlusParser#print}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrint(SimpLanPlusParser.PrintContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpLanPlusParser#ret}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRet(SimpLanPlusParser.RetContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpLanPlusParser#ite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIte(SimpLanPlusParser.IteContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpLanPlusParser#call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall(SimpLanPlusParser.CallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code baseExp}
	 * labeled alternative in {@link SimpLanPlusParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseExp(SimpLanPlusParser.BaseExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binExp}
	 * labeled alternative in {@link SimpLanPlusParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinExp(SimpLanPlusParser.BinExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code derExp}
	 * labeled alternative in {@link SimpLanPlusParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDerExp(SimpLanPlusParser.DerExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code newExp}
	 * labeled alternative in {@link SimpLanPlusParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewExp(SimpLanPlusParser.NewExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valExp}
	 * labeled alternative in {@link SimpLanPlusParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValExp(SimpLanPlusParser.ValExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code negExp}
	 * labeled alternative in {@link SimpLanPlusParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegExp(SimpLanPlusParser.NegExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolExp}
	 * labeled alternative in {@link SimpLanPlusParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolExp(SimpLanPlusParser.BoolExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code callExp}
	 * labeled alternative in {@link SimpLanPlusParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallExp(SimpLanPlusParser.CallExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExp}
	 * labeled alternative in {@link SimpLanPlusParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExp(SimpLanPlusParser.NotExpContext ctx);
}