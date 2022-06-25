// Generated from SVM.g4 by ANTLR 4.4
package svm;

import java.util.List;
import java.util.ArrayList;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SVMLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__1=1, T__0=2, PUSH=3, POP=4, ADD=5, SUB=6, MULT=7, DIV=8, ADDI=9, SUBI=10, 
		MULTI=11, DIVI=12, LI=13, LB=14, STOREW=15, LOADW=16, BRANCH=17, BRANCHEQ=18, 
		BRANCHLESSEQ=19, JR=20, JAL=21, PRINT=22, DELETION=23, MOVE=24, AND=25, 
		OR=26, NOT=27, ANDB=28, ORB=29, NOTB=30, HALT=31, REGISTER=32, COL=33, 
		LABEL=34, NUMBER=35, BOOL=36, WHITESP=37, ERR=38;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'", "'\\u0018'", 
		"'\\u0019'", "'\\u001A'", "'\\u001B'", "'\\u001C'", "'\\u001D'", "'\\u001E'", 
		"'\\u001F'", "' '", "'!'", "'\"'", "'#'", "'$'", "'%'", "'&'"
	};
	public static final String[] ruleNames = {
		"T__1", "T__0", "PUSH", "POP", "ADD", "SUB", "MULT", "DIV", "ADDI", "SUBI", 
		"MULTI", "DIVI", "LI", "LB", "STOREW", "LOADW", "BRANCH", "BRANCHEQ", 
		"BRANCHLESSEQ", "JR", "JAL", "PRINT", "DELETION", "MOVE", "AND", "OR", 
		"NOT", "ANDB", "ORB", "NOTB", "HALT", "REGISTER", "COL", "LABEL", "NUMBER", 
		"BOOL", "WHITESP", "ERR"
	};


	private List<String> errors = new ArrayList<>();
		
		public int errorCount() {
			return errors.size();
		}
		
		public ArrayList getErrors(){
			return new ArrayList(errors);
		}


	public SVMLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SVM.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 37: ERR_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void ERR_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:  errors.add("Invalid character: "+ getText());   break;
		}
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2(\u011b\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\3\2\3\2\3\3\3\3\3\4\3\4\3"+
		"\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b"+
		"\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17"+
		"\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\23\3\24"+
		"\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27"+
		"\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\32"+
		"\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\36\3\36"+
		"\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3"+
		"!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\5!\u00e5\n!\3"+
		"\"\3\"\3#\3#\7#\u00eb\n#\f#\16#\u00ee\13#\3#\3#\3#\7#\u00f3\n#\f#\16#"+
		"\u00f6\13#\3$\3$\5$\u00fa\n$\3$\3$\7$\u00fe\n$\f$\16$\u0101\13$\5$\u0103"+
		"\n$\3%\3%\3%\3%\3%\3%\3%\3%\3%\5%\u010e\n%\3&\6&\u0111\n&\r&\16&\u0112"+
		"\3&\3&\3\'\3\'\3\'\3\'\3\'\2\2(\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23"+
		"\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31"+
		"\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(\3\2\4\4\2C\\c|\5"+
		"\2\13\f\17\17\"\"\u0128\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2"+
		"\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2"+
		"\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3"+
		"\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2"+
		"\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\3O\3\2\2\2\5"+
		"Q\3\2\2\2\7S\3\2\2\2\tX\3\2\2\2\13\\\3\2\2\2\r`\3\2\2\2\17d\3\2\2\2\21"+
		"i\3\2\2\2\23m\3\2\2\2\25r\3\2\2\2\27w\3\2\2\2\31}\3\2\2\2\33\u0082\3\2"+
		"\2\2\35\u0085\3\2\2\2\37\u0088\3\2\2\2!\u008b\3\2\2\2#\u008e\3\2\2\2%"+
		"\u0090\3\2\2\2\'\u0094\3\2\2\2)\u0099\3\2\2\2+\u009c\3\2\2\2-\u00a0\3"+
		"\2\2\2/\u00a6\3\2\2\2\61\u00aa\3\2\2\2\63\u00ad\3\2\2\2\65\u00b1\3\2\2"+
		"\2\67\u00b4\3\2\2\29\u00b8\3\2\2\2;\u00bd\3\2\2\2=\u00c1\3\2\2\2?\u00c6"+
		"\3\2\2\2A\u00e4\3\2\2\2C\u00e6\3\2\2\2E\u00e8\3\2\2\2G\u0102\3\2\2\2I"+
		"\u010d\3\2\2\2K\u0110\3\2\2\2M\u0116\3\2\2\2OP\7*\2\2P\4\3\2\2\2QR\7+"+
		"\2\2R\6\3\2\2\2ST\7r\2\2TU\7w\2\2UV\7u\2\2VW\7j\2\2W\b\3\2\2\2XY\7r\2"+
		"\2YZ\7q\2\2Z[\7r\2\2[\n\3\2\2\2\\]\7c\2\2]^\7f\2\2^_\7f\2\2_\f\3\2\2\2"+
		"`a\7u\2\2ab\7w\2\2bc\7d\2\2c\16\3\2\2\2de\7o\2\2ef\7w\2\2fg\7n\2\2gh\7"+
		"v\2\2h\20\3\2\2\2ij\7f\2\2jk\7k\2\2kl\7x\2\2l\22\3\2\2\2mn\7c\2\2no\7"+
		"f\2\2op\7f\2\2pq\7k\2\2q\24\3\2\2\2rs\7u\2\2st\7w\2\2tu\7d\2\2uv\7k\2"+
		"\2v\26\3\2\2\2wx\7o\2\2xy\7w\2\2yz\7n\2\2z{\7v\2\2{|\7k\2\2|\30\3\2\2"+
		"\2}~\7f\2\2~\177\7k\2\2\177\u0080\7x\2\2\u0080\u0081\7k\2\2\u0081\32\3"+
		"\2\2\2\u0082\u0083\7n\2\2\u0083\u0084\7k\2\2\u0084\34\3\2\2\2\u0085\u0086"+
		"\7n\2\2\u0086\u0087\7d\2\2\u0087\36\3\2\2\2\u0088\u0089\7u\2\2\u0089\u008a"+
		"\7y\2\2\u008a \3\2\2\2\u008b\u008c\7n\2\2\u008c\u008d\7y\2\2\u008d\"\3"+
		"\2\2\2\u008e\u008f\7d\2\2\u008f$\3\2\2\2\u0090\u0091\7d\2\2\u0091\u0092"+
		"\7g\2\2\u0092\u0093\7s\2\2\u0093&\3\2\2\2\u0094\u0095\7d\2\2\u0095\u0096"+
		"\7n\2\2\u0096\u0097\7g\2\2\u0097\u0098\7s\2\2\u0098(\3\2\2\2\u0099\u009a"+
		"\7l\2\2\u009a\u009b\7t\2\2\u009b*\3\2\2\2\u009c\u009d\7l\2\2\u009d\u009e"+
		"\7c\2\2\u009e\u009f\7n\2\2\u009f,\3\2\2\2\u00a0\u00a1\7r\2\2\u00a1\u00a2"+
		"\7t\2\2\u00a2\u00a3\7k\2\2\u00a3\u00a4\7p\2\2\u00a4\u00a5\7v\2\2\u00a5"+
		".\3\2\2\2\u00a6\u00a7\7f\2\2\u00a7\u00a8\7g\2\2\u00a8\u00a9\7n\2\2\u00a9"+
		"\60\3\2\2\2\u00aa\u00ab\7o\2\2\u00ab\u00ac\7x\2\2\u00ac\62\3\2\2\2\u00ad"+
		"\u00ae\7c\2\2\u00ae\u00af\7p\2\2\u00af\u00b0\7f\2\2\u00b0\64\3\2\2\2\u00b1"+
		"\u00b2\7q\2\2\u00b2\u00b3\7t\2\2\u00b3\66\3\2\2\2\u00b4\u00b5\7p\2\2\u00b5"+
		"\u00b6\7q\2\2\u00b6\u00b7\7v\2\2\u00b78\3\2\2\2\u00b8\u00b9\7c\2\2\u00b9"+
		"\u00ba\7p\2\2\u00ba\u00bb\7f\2\2\u00bb\u00bc\7d\2\2\u00bc:\3\2\2\2\u00bd"+
		"\u00be\7q\2\2\u00be\u00bf\7t\2\2\u00bf\u00c0\7d\2\2\u00c0<\3\2\2\2\u00c1"+
		"\u00c2\7p\2\2\u00c2\u00c3\7q\2\2\u00c3\u00c4\7v\2\2\u00c4\u00c5\7d\2\2"+
		"\u00c5>\3\2\2\2\u00c6\u00c7\7j\2\2\u00c7\u00c8\7c\2\2\u00c8\u00c9\7n\2"+
		"\2\u00c9\u00ca\7v\2\2\u00ca@\3\2\2\2\u00cb\u00cc\7&\2\2\u00cc\u00cd\7"+
		"c\2\2\u00cd\u00e5\7\62\2\2\u00ce\u00cf\7&\2\2\u00cf\u00d0\7v\2\2\u00d0"+
		"\u00e5\7\63\2\2\u00d1\u00d2\7&\2\2\u00d2\u00d3\7u\2\2\u00d3\u00e5\7r\2"+
		"\2\u00d4\u00d5\7&\2\2\u00d5\u00d6\7h\2\2\u00d6\u00e5\7r\2\2\u00d7\u00d8"+
		"\7&\2\2\u00d8\u00d9\7c\2\2\u00d9\u00e5\7n\2\2\u00da\u00db\7&\2\2\u00db"+
		"\u00dc\7t\2\2\u00dc\u00e5\7c\2\2\u00dd\u00de\7&\2\2\u00de\u00df\7j\2\2"+
		"\u00df\u00e5\7r\2\2\u00e0\u00e1\7&\2\2\u00e1\u00e2\7t\2\2\u00e2\u00e3"+
		"\7g\2\2\u00e3\u00e5\7v\2\2\u00e4\u00cb\3\2\2\2\u00e4\u00ce\3\2\2\2\u00e4"+
		"\u00d1\3\2\2\2\u00e4\u00d4\3\2\2\2\u00e4\u00d7\3\2\2\2\u00e4\u00da\3\2"+
		"\2\2\u00e4\u00dd\3\2\2\2\u00e4\u00e0\3\2\2\2\u00e5B\3\2\2\2\u00e6\u00e7"+
		"\7<\2\2\u00e7D\3\2\2\2\u00e8\u00ec\t\2\2\2\u00e9\u00eb\t\2\2\2\u00ea\u00e9"+
		"\3\2\2\2\u00eb\u00ee\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed"+
		"\u00ef\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ef\u00f0\7a\2\2\u00f0\u00f4\4\62"+
		";\2\u00f1\u00f3\4\62;\2\u00f2\u00f1\3\2\2\2\u00f3\u00f6\3\2\2\2\u00f4"+
		"\u00f2\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5F\3\2\2\2\u00f6\u00f4\3\2\2\2"+
		"\u00f7\u0103\7\62\2\2\u00f8\u00fa\7/\2\2\u00f9\u00f8\3\2\2\2\u00f9\u00fa"+
		"\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00ff\4\63;\2\u00fc\u00fe\4\62;\2\u00fd"+
		"\u00fc\3\2\2\2\u00fe\u0101\3\2\2\2\u00ff\u00fd\3\2\2\2\u00ff\u0100\3\2"+
		"\2\2\u0100\u0103\3\2\2\2\u0101\u00ff\3\2\2\2\u0102\u00f7\3\2\2\2\u0102"+
		"\u00f9\3\2\2\2\u0103H\3\2\2\2\u0104\u0105\7h\2\2\u0105\u0106\7c\2\2\u0106"+
		"\u0107\7n\2\2\u0107\u0108\7u\2\2\u0108\u010e\7g\2\2\u0109\u010a\7v\2\2"+
		"\u010a\u010b\7t\2\2\u010b\u010c\7w\2\2\u010c\u010e\7g\2\2\u010d\u0104"+
		"\3\2\2\2\u010d\u0109\3\2\2\2\u010eJ\3\2\2\2\u010f\u0111\t\3\2\2\u0110"+
		"\u010f\3\2\2\2\u0111\u0112\3\2\2\2\u0112\u0110\3\2\2\2\u0112\u0113\3\2"+
		"\2\2\u0113\u0114\3\2\2\2\u0114\u0115\b&\2\2\u0115L\3\2\2\2\u0116\u0117"+
		"\13\2\2\2\u0117\u0118\b\'\3\2\u0118\u0119\3\2\2\2\u0119\u011a\b\'\2\2"+
		"\u011aN\3\2\2\2\13\2\u00e4\u00ec\u00f4\u00f9\u00ff\u0102\u010d\u0112\4"+
		"\2\3\2\3\'\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}