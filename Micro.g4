grammar Micro;

/*
    Harshita Kodali, hkodali
    Amala Chirayil, achirayi

    Step0: Grammar File
    Name and format changed form step2.
    OLD NAME: MicroParger.g4
*/

//PARSER -  STEP2
//PROGRAM
program			: PROGRAM id BEGIN pgm_body END;
id				: IDENTIFIER;
pgm_body		: decl func_declarations;
decl			: (string_decl | var_decl)*;

//GLOBAL STRINGS
string_decl		: STRING id ASSIGNMENT STRINGLITERAL SEMI;

//VARIABLE DECLARATIONS
var_decl		: var_type id_list SEMI;
var_type		: FLOAT | INT;
any_type		: VOID | var_type;
id_list			: id (COMMA id)*;


//FUNCTION PARAMETER LIST
param_decl_list	: (param_decl (COMMA param_decl)*)?;
param_decl		: var_type id;

//FUNCTION DECLARATIONS
func_declarations: func_decl*;
func_decl		: FUNCTION any_type id LPAREN param_decl_list RPAREN BEGIN func_body END;
func_body		: decl stmt_list;

//STATEMENTS
stmt_list		: stmt*;
stmt			: base_stmt | if_stmt | while_stmt;
base_stmt		: assign_stmt | read_stmt | write_stmt | return_stmt;

//BASIC STATEMENTS
assign_stmt		: id ASSIGNMENT expr SEMI;
read_stmt		: READ LPAREN id_list RPAREN SEMI;
write_stmt		: WRITE LPAREN id_list RPAREN SEMI;
return_stmt		: RETURN expr SEMI;

//EXPRESSIONS
expr			: (factor addop)* factor;
factor			: (postfix_expr mulop)* postfix_expr;
postfix_expr	: primary | call_expr;
call_expr		: id LPAREN expr_list RPAREN;
expr_list		: (expr (COMMA expr)*)?;
primary			: (LPAREN expr RPAREN) | id | INTLITERAL | FLOATLITERAL;
addop			: ADD | SUBTRACT;
mulop			: MULTIPLY | DIVIDE;

//COMPLEX STATEMENTS
if_stmt			: IF LPAREN cond RPAREN decl stmt_list else_part ENDIF;
else_part		: (ELSE decl stmt_list)?;
cond			: expr compop expr;
compop			: LTHAN | GTHAN | LTHANEQ | GTHANEQ | NEQUAL | EQUAL;

while_stmt		: WHILE LPAREN cond RPAREN decl stmt_list ENDWHILE;

//SCANNER -  STEP1
PROGRAM       : 'PROGRAM';
BEGIN         : 'BEGIN';
END			  : 'END';
FUNCTION      : 'FUNCTION';
READ          : 'READ';
WRITE         : 'WRITE';
IF            : 'IF';
ELSE          : 'ELSE';
ENDIF         : 'ENDIF';
WHILE         : 'WHILE';
ENDWHILE      : 'ENDWHILE';
RETURN        : 'RETURN';
INT           : 'INT';
VOID          : 'VOID';
STRING        : 'STRING';
FLOAT         : 'FLOAT';

ASSIGNMENT    : ':=';
ADD           : '+';
SUBTRACT      : '-';
MULTIPLY      : '*';
DIVIDE        : '/';
EQUAL         : '=';
NEQUAL        : '!=';
LTHAN         : '<';
GTHAN         : '>';
LTHANEQ       : '<=';
GTHANEQ       : '>=';
LPAREN        : '(';
RPAREN        : ')';
SEMI          : ';';
COMMA         : ',';

IDENTIFIER    : [a-zA-Z] ([a-zA-Z0-9])*;
FLOATLITERAL  : [0-9]*? '.' [0-9]+;
INTLITERAL    : [0-9]+;
STRINGLITERAL : '"' ~['"']*? '"';
COMMENT       : ('--' ~[\r\n]*)-> skip;

WS : [ \t\n\r]+ -> skip;