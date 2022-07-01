// Generated from SVM.g4 by ANTLR 4.9.3
package svm;

import java.util.List;
import java.util.ArrayList;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SVMParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, PUSH=3, POP=4, ADD=5, SUB=6, MULT=7, DIV=8, ADDI=9, SUBI=10, 
		MULTI=11, DIVI=12, LI=13, LB=14, STOREW=15, LOADW=16, BRANCH=17, BRANCHEQ=18, 
		BRANCHLESSEQ=19, JR=20, JAL=21, PRINT=22, DELETION=23, MOVE=24, AND=25, 
		OR=26, NOT=27, ANDB=28, ORB=29, NOTB=30, HALT=31, REGISTER=32, COL=33, 
		LABEL=34, NUMBER=35, BOOL=36, WHITESP=37, ERR=38;
	public static final int
		RULE_assembly = 0, RULE_instruction = 1;
	private static String[] makeRuleNames() {
		return new String[] {
			"assembly", "instruction"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'push'", "'pop'", "'add'", "'sub'", "'mult'", "'div'", 
			"'addi'", "'subi'", "'multi'", "'divi'", "'li'", "'lb'", "'sw'", "'lw'", 
			"'b'", "'beq'", "'bleq'", "'jr'", "'jal'", "'print'", "'del'", "'mv'", 
			"'and'", "'or'", "'not'", "'andb'", "'orb'", "'notb'", "'halt'", null, 
			"':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "PUSH", "POP", "ADD", "SUB", "MULT", "DIV", "ADDI", 
			"SUBI", "MULTI", "DIVI", "LI", "LB", "STOREW", "LOADW", "BRANCH", "BRANCHEQ", 
			"BRANCHLESSEQ", "JR", "JAL", "PRINT", "DELETION", "MOVE", "AND", "OR", 
			"NOT", "ANDB", "ORB", "NOTB", "HALT", "REGISTER", "COL", "LABEL", "NUMBER", 
			"BOOL", "WHITESP", "ERR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "SVM.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SVMParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class AssemblyContext extends ParserRuleContext {
		public List<InstructionContext> instruction() {
			return getRuleContexts(InstructionContext.class);
		}
		public InstructionContext instruction(int i) {
			return getRuleContext(InstructionContext.class,i);
		}
		public AssemblyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assembly; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SVMVisitor ) return ((SVMVisitor<? extends T>)visitor).visitAssembly(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssemblyContext assembly() throws RecognitionException {
		AssemblyContext _localctx = new AssemblyContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_assembly);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(7);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PUSH) | (1L << POP) | (1L << ADD) | (1L << SUB) | (1L << MULT) | (1L << DIV) | (1L << ADDI) | (1L << SUBI) | (1L << MULTI) | (1L << DIVI) | (1L << LI) | (1L << LB) | (1L << STOREW) | (1L << LOADW) | (1L << BRANCH) | (1L << BRANCHEQ) | (1L << BRANCHLESSEQ) | (1L << JR) | (1L << JAL) | (1L << PRINT) | (1L << DELETION) | (1L << MOVE) | (1L << AND) | (1L << OR) | (1L << NOT) | (1L << ANDB) | (1L << ORB) | (1L << NOTB) | (1L << HALT) | (1L << LABEL))) != 0)) {
				{
				{
				setState(4);
				instruction();
				}
				}
				setState(9);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InstructionContext extends ParserRuleContext {
		public Token reg;
		public Token res;
		public Token term1;
		public Token term2;
		public Token term;
		public Token value;
		public Token offset;
		public Token address;
		public Token l;
		public Token to;
		public Token from;
		public TerminalNode PUSH() { return getToken(SVMParser.PUSH, 0); }
		public TerminalNode POP() { return getToken(SVMParser.POP, 0); }
		public TerminalNode ADD() { return getToken(SVMParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(SVMParser.SUB, 0); }
		public TerminalNode MULT() { return getToken(SVMParser.MULT, 0); }
		public TerminalNode DIV() { return getToken(SVMParser.DIV, 0); }
		public TerminalNode ADDI() { return getToken(SVMParser.ADDI, 0); }
		public TerminalNode SUBI() { return getToken(SVMParser.SUBI, 0); }
		public TerminalNode MULTI() { return getToken(SVMParser.MULTI, 0); }
		public TerminalNode DIVI() { return getToken(SVMParser.DIVI, 0); }
		public TerminalNode LI() { return getToken(SVMParser.LI, 0); }
		public TerminalNode LB() { return getToken(SVMParser.LB, 0); }
		public TerminalNode STOREW() { return getToken(SVMParser.STOREW, 0); }
		public TerminalNode LOADW() { return getToken(SVMParser.LOADW, 0); }
		public TerminalNode COL() { return getToken(SVMParser.COL, 0); }
		public TerminalNode BRANCH() { return getToken(SVMParser.BRANCH, 0); }
		public TerminalNode BRANCHEQ() { return getToken(SVMParser.BRANCHEQ, 0); }
		public TerminalNode BRANCHLESSEQ() { return getToken(SVMParser.BRANCHLESSEQ, 0); }
		public TerminalNode JR() { return getToken(SVMParser.JR, 0); }
		public TerminalNode JAL() { return getToken(SVMParser.JAL, 0); }
		public TerminalNode MOVE() { return getToken(SVMParser.MOVE, 0); }
		public TerminalNode PRINT() { return getToken(SVMParser.PRINT, 0); }
		public TerminalNode DELETION() { return getToken(SVMParser.DELETION, 0); }
		public TerminalNode AND() { return getToken(SVMParser.AND, 0); }
		public TerminalNode OR() { return getToken(SVMParser.OR, 0); }
		public TerminalNode NOT() { return getToken(SVMParser.NOT, 0); }
		public TerminalNode ANDB() { return getToken(SVMParser.ANDB, 0); }
		public TerminalNode ORB() { return getToken(SVMParser.ORB, 0); }
		public TerminalNode NOTB() { return getToken(SVMParser.NOTB, 0); }
		public TerminalNode HALT() { return getToken(SVMParser.HALT, 0); }
		public List<TerminalNode> REGISTER() { return getTokens(SVMParser.REGISTER); }
		public TerminalNode REGISTER(int i) {
			return getToken(SVMParser.REGISTER, i);
		}
		public TerminalNode NUMBER() { return getToken(SVMParser.NUMBER, 0); }
		public TerminalNode BOOL() { return getToken(SVMParser.BOOL, 0); }
		public TerminalNode LABEL() { return getToken(SVMParser.LABEL, 0); }
		public InstructionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instruction; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SVMVisitor ) return ((SVMVisitor<? extends T>)visitor).visitInstruction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstructionContext instruction() throws RecognitionException {
		InstructionContext _localctx = new InstructionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_instruction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PUSH:
				{
				setState(10);
				match(PUSH);
				setState(11);
				((InstructionContext)_localctx).reg = match(REGISTER);
				}
				break;
			case POP:
				{
				setState(12);
				match(POP);
				}
				break;
			case ADD:
				{
				setState(13);
				match(ADD);
				setState(14);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(15);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(16);
				((InstructionContext)_localctx).term2 = match(REGISTER);
				}
				break;
			case SUB:
				{
				setState(17);
				match(SUB);
				setState(18);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(19);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(20);
				((InstructionContext)_localctx).term2 = match(REGISTER);
				}
				break;
			case MULT:
				{
				setState(21);
				match(MULT);
				setState(22);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(23);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(24);
				((InstructionContext)_localctx).term2 = match(REGISTER);
				}
				break;
			case DIV:
				{
				setState(25);
				match(DIV);
				setState(26);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(27);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(28);
				((InstructionContext)_localctx).term2 = match(REGISTER);
				}
				break;
			case ADDI:
				{
				setState(29);
				match(ADDI);
				setState(30);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(31);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(32);
				((InstructionContext)_localctx).term2 = match(NUMBER);
				}
				break;
			case SUBI:
				{
				setState(33);
				match(SUBI);
				setState(34);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(35);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(36);
				((InstructionContext)_localctx).term2 = match(NUMBER);
				}
				break;
			case MULTI:
				{
				setState(37);
				match(MULTI);
				setState(38);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(39);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(40);
				((InstructionContext)_localctx).term2 = match(NUMBER);
				}
				break;
			case DIVI:
				{
				setState(41);
				match(DIVI);
				setState(42);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(43);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(44);
				((InstructionContext)_localctx).term2 = match(NUMBER);
				}
				break;
			case LI:
				{
				setState(45);
				match(LI);
				setState(46);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(47);
				((InstructionContext)_localctx).term = match(NUMBER);
				}
				break;
			case LB:
				{
				setState(48);
				match(LB);
				setState(49);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(50);
				((InstructionContext)_localctx).term = match(BOOL);
				}
				break;
			case STOREW:
				{
				setState(51);
				match(STOREW);
				setState(52);
				((InstructionContext)_localctx).value = match(REGISTER);
				setState(53);
				((InstructionContext)_localctx).offset = match(NUMBER);
				setState(54);
				match(T__0);
				setState(55);
				((InstructionContext)_localctx).address = match(REGISTER);
				setState(56);
				match(T__1);
				}
				break;
			case LOADW:
				{
				setState(57);
				match(LOADW);
				setState(58);
				((InstructionContext)_localctx).value = match(REGISTER);
				setState(59);
				((InstructionContext)_localctx).offset = match(NUMBER);
				setState(60);
				match(T__0);
				setState(61);
				((InstructionContext)_localctx).address = match(REGISTER);
				setState(62);
				match(T__1);
				}
				break;
			case LABEL:
				{
				setState(63);
				((InstructionContext)_localctx).l = match(LABEL);
				setState(64);
				match(COL);
				}
				break;
			case BRANCH:
				{
				setState(65);
				match(BRANCH);
				setState(66);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case BRANCHEQ:
				{
				setState(67);
				match(BRANCHEQ);
				setState(68);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(69);
				((InstructionContext)_localctx).term2 = match(REGISTER);
				setState(70);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case BRANCHLESSEQ:
				{
				setState(71);
				match(BRANCHLESSEQ);
				setState(72);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(73);
				((InstructionContext)_localctx).term2 = match(REGISTER);
				setState(74);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case JR:
				{
				setState(75);
				match(JR);
				setState(76);
				((InstructionContext)_localctx).reg = match(REGISTER);
				}
				break;
			case JAL:
				{
				setState(77);
				match(JAL);
				setState(78);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case MOVE:
				{
				setState(79);
				match(MOVE);
				setState(80);
				((InstructionContext)_localctx).to = match(REGISTER);
				setState(81);
				((InstructionContext)_localctx).from = match(REGISTER);
				}
				break;
			case PRINT:
				{
				setState(82);
				match(PRINT);
				setState(83);
				((InstructionContext)_localctx).reg = match(REGISTER);
				setState(84);
				((InstructionContext)_localctx).term1 = match(NUMBER);
				}
				break;
			case DELETION:
				{
				setState(85);
				match(DELETION);
				setState(86);
				((InstructionContext)_localctx).reg = match(REGISTER);
				}
				break;
			case AND:
				{
				setState(87);
				match(AND);
				setState(88);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(89);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(90);
				((InstructionContext)_localctx).term2 = match(REGISTER);
				}
				break;
			case OR:
				{
				setState(91);
				match(OR);
				setState(92);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(93);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(94);
				((InstructionContext)_localctx).term2 = match(REGISTER);
				}
				break;
			case NOT:
				{
				setState(95);
				match(NOT);
				setState(96);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(97);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				}
				break;
			case ANDB:
				{
				setState(98);
				match(ANDB);
				setState(99);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(100);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(101);
				((InstructionContext)_localctx).term2 = match(BOOL);
				}
				break;
			case ORB:
				{
				setState(102);
				match(ORB);
				setState(103);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(104);
				((InstructionContext)_localctx).term1 = match(REGISTER);
				setState(105);
				((InstructionContext)_localctx).term2 = match(BOOL);
				}
				break;
			case NOTB:
				{
				setState(106);
				match(NOTB);
				setState(107);
				((InstructionContext)_localctx).res = match(REGISTER);
				setState(108);
				((InstructionContext)_localctx).term1 = match(BOOL);
				}
				break;
			case HALT:
				{
				setState(109);
				match(HALT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3(s\4\2\t\2\4\3\t\3"+
		"\3\2\7\2\b\n\2\f\2\16\2\13\13\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\5\3q\n\3\3\3\2\2\4\2\4\2\2\2\u008e\2\t\3\2\2\2\4p\3\2\2\2"+
		"\6\b\5\4\3\2\7\6\3\2\2\2\b\13\3\2\2\2\t\7\3\2\2\2\t\n\3\2\2\2\n\3\3\2"+
		"\2\2\13\t\3\2\2\2\f\r\7\5\2\2\rq\7\"\2\2\16q\7\6\2\2\17\20\7\7\2\2\20"+
		"\21\7\"\2\2\21\22\7\"\2\2\22q\7\"\2\2\23\24\7\b\2\2\24\25\7\"\2\2\25\26"+
		"\7\"\2\2\26q\7\"\2\2\27\30\7\t\2\2\30\31\7\"\2\2\31\32\7\"\2\2\32q\7\""+
		"\2\2\33\34\7\n\2\2\34\35\7\"\2\2\35\36\7\"\2\2\36q\7\"\2\2\37 \7\13\2"+
		"\2 !\7\"\2\2!\"\7\"\2\2\"q\7%\2\2#$\7\f\2\2$%\7\"\2\2%&\7\"\2\2&q\7%\2"+
		"\2\'(\7\r\2\2()\7\"\2\2)*\7\"\2\2*q\7%\2\2+,\7\16\2\2,-\7\"\2\2-.\7\""+
		"\2\2.q\7%\2\2/\60\7\17\2\2\60\61\7\"\2\2\61q\7%\2\2\62\63\7\20\2\2\63"+
		"\64\7\"\2\2\64q\7&\2\2\65\66\7\21\2\2\66\67\7\"\2\2\678\7%\2\289\7\3\2"+
		"\29:\7\"\2\2:q\7\4\2\2;<\7\22\2\2<=\7\"\2\2=>\7%\2\2>?\7\3\2\2?@\7\"\2"+
		"\2@q\7\4\2\2AB\7$\2\2Bq\7#\2\2CD\7\23\2\2Dq\7$\2\2EF\7\24\2\2FG\7\"\2"+
		"\2GH\7\"\2\2Hq\7$\2\2IJ\7\25\2\2JK\7\"\2\2KL\7\"\2\2Lq\7$\2\2MN\7\26\2"+
		"\2Nq\7\"\2\2OP\7\27\2\2Pq\7$\2\2QR\7\32\2\2RS\7\"\2\2Sq\7\"\2\2TU\7\30"+
		"\2\2UV\7\"\2\2Vq\7%\2\2WX\7\31\2\2Xq\7\"\2\2YZ\7\33\2\2Z[\7\"\2\2[\\\7"+
		"\"\2\2\\q\7\"\2\2]^\7\34\2\2^_\7\"\2\2_`\7\"\2\2`q\7\"\2\2ab\7\35\2\2"+
		"bc\7\"\2\2cq\7\"\2\2de\7\36\2\2ef\7\"\2\2fg\7\"\2\2gq\7&\2\2hi\7\37\2"+
		"\2ij\7\"\2\2jk\7\"\2\2kq\7&\2\2lm\7 \2\2mn\7\"\2\2nq\7&\2\2oq\7!\2\2p"+
		"\f\3\2\2\2p\16\3\2\2\2p\17\3\2\2\2p\23\3\2\2\2p\27\3\2\2\2p\33\3\2\2\2"+
		"p\37\3\2\2\2p#\3\2\2\2p\'\3\2\2\2p+\3\2\2\2p/\3\2\2\2p\62\3\2\2\2p\65"+
		"\3\2\2\2p;\3\2\2\2pA\3\2\2\2pC\3\2\2\2pE\3\2\2\2pI\3\2\2\2pM\3\2\2\2p"+
		"O\3\2\2\2pQ\3\2\2\2pT\3\2\2\2pW\3\2\2\2pY\3\2\2\2p]\3\2\2\2pa\3\2\2\2"+
		"pd\3\2\2\2ph\3\2\2\2pl\3\2\2\2po\3\2\2\2q\5\3\2\2\2\4\tp";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}