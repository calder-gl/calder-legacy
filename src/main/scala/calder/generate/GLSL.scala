// DO NOT MODIFY: these classes are automatically generated!

package calder.generate

// Tokens

class RightParen

class RightAssign

class Switch

class Ivec4

class Caret

class ISampler2DArray

class ISamplercube

class Centroid

class USampler2D

class Sampler2Dshadow

class AddAssign

class DivAssign

class Discard

class Mat3x2

class OrAssign

class OrOp

class Const

class Question

class Slash

class Mat2x3

class NeOp

class Float

class Int

class Mat2x4

class Equal

class Vec3

class Ampersand

class Semicolon

class GeOp

class Default

class Mat2

class Invariant

class Struct

class In

class VerticalBar

class Comma

class While

class Bvec2

class Vec2

class Mat4x2

class LowPrecision

class DecOp

class LeftOp

class Break

class Bvec3

class Uvec2

class Mat4

class AndAssign

class LeftAssign

class Smooth

class Inout

class RightOp

class LeftBracket

class RightAngle

class Sampler2D

class Mat4x3

class Bang

class Dash

class Percent

class Mat3x4

class Case

class If

class USamplercube

class Tilde

class Star

class LeftAngle

class Bvec4

class Ivec2

class For

class Do

class IncOp

class Plus

class Return

class Else

class Bool

class ISampler2D

class ISampler3D

class MulAssign

class LeftParen

class Samplercube

class XorOp

class Mat3

class Uint

class USampler3D

class LeftBrace

class Precision

class Uvec3

class Colon

class Void

class AndOp

class Dot

class Vec4

class Sampler2DArray

class Out

class Uvec4

class Ivec3

class USampler2DArray

class LeOp

class RightBracket

class HighPrecision

class ModAssign

class Mat4x4

class MediumPrecision

class RightBrace

class Uniform

class Sampler3D

class XorAssign

class Continue

class Flat

class Layout

class Mat3x3

class Samplercubeshadow

class SubAssign

class EqOp

class Sampler2DArrayshadow

class Mat2x2

// Literals

class TypeName(value: String) {
  def source() = s"${value} "
}

class Identifier(value: String) {
  def source() = s"${value} "
}

class Floatconstant(value: String) {
  def source() = s"${value} "
}

class Intconstant(value: String) {
  def source() = s"${value} "
}

class Uintconstant(value: String) {
  def source() = s"${value} "
}

class Boolconstant(value: String) {
  def source() = s"${value} "
}

class FieldSelection(value: String) {
  def source() = s"${value} "
}

// Rules

class VariableIdentifier private (_source: String) {
  def this(IDENTIFIER: Identifier) = this(s"${IDENTIFIER.source()}")
  def source() = _source
}

class PrimaryExpression private (_source: String) {
  def this(variableIdentifier: VariableIdentifier) = this(s"${variableIdentifier.source()}")
  def this(INTCONSTANT: Intconstant) = this(s"${INTCONSTANT.source()}")
  def this(UINTCONSTANT: Uintconstant) = this(s"${UINTCONSTANT.source()}")
  def this(FLOATCONSTANT: Floatconstant) = this(s"${FLOATCONSTANT.source()}")
  def this(BOOLCONSTANT: Boolconstant) = this(s"${BOOLCONSTANT.source()}")
  def this(expression: Expression) = this(s"( ${expression.source()}) ")
  def source() = _source
}

class PostfixExpression private (_source: String) {
  def this(primaryExpression: PrimaryExpression) = this(s"${primaryExpression.source()}")
  def this(postfixExpression: PostfixExpression, integerExpression: IntegerExpression, functionCall: FunctionCall) =
    this(s"${postfixExpression.source()}[ ${integerExpression.source()}] ${functionCall.source()}")
  def this(postfixExpression: PostfixExpression, FIELD_SELECTION: FieldSelection) =
    this(s"${postfixExpression.source()}. ${FIELD_SELECTION.source()}")
  def this(postfixExpression: PostfixExpression, INC_OP: IncOp) = this(s"${postfixExpression.source()}++ ")
  def this(postfixExpression: PostfixExpression) = this(s"${postfixExpression.source()}-- ")
  def source() = _source
}

class IntegerExpression private (_source: String) {
  def this(expression: Expression) = this(s"${expression.source()}")
  def source() = _source
}

class FunctionCall private (_source: String) {
  def this(functionCallOrMethod: FunctionCallOrMethod) = this(s"${functionCallOrMethod.source()}")
  def source() = _source
}

class FunctionCallOrMethod private (_source: String) {
  def this(functionCallGeneric: FunctionCallGeneric) = this(s"${functionCallGeneric.source()}")
  def this(postfixExpression: PostfixExpression, functionCallGeneric: FunctionCallGeneric) =
    this(s"${postfixExpression.source()}. ${functionCallGeneric.source()}")
  def source() = _source
}

class FunctionCallGeneric private (_source: String) {
  def this(functionCallHeaderWithParameters: FunctionCallHeaderWithParameters) =
    this(s"${functionCallHeaderWithParameters.source()}) ")
  def this(functionCallHeaderNoParameters: FunctionCallHeaderNoParameters) =
    this(s"${functionCallHeaderNoParameters.source()}) ")
  def source() = _source
}

class FunctionCallHeaderNoParameters private (_source: String) {
  def this(functionCallHeader: FunctionCallHeader, VOID: Void) = this(s"${functionCallHeader.source()}void ")
  def this(functionCallHeader: FunctionCallHeader) = this(s"${functionCallHeader.source()}")
  def source() = _source
}

class FunctionCallHeaderWithParameters private (_source: String) {
  def this(functionCallHeader: FunctionCallHeader, assignmentExpression: AssignmentExpression) =
    this(s"${functionCallHeader.source()}${assignmentExpression.source()}")
  def this(functionCallHeaderWithParameters: FunctionCallHeaderWithParameters,
           assignmentExpression: AssignmentExpression) =
    this(s"${functionCallHeaderWithParameters.source()}, ${assignmentExpression.source()}")
  def source() = _source
}

class FunctionCallHeader private (_source: String) {
  def this(functionIdentifier: FunctionIdentifier) = this(s"${functionIdentifier.source()}( ")
  def source() = _source
}

class FunctionIdentifier private (_source: String) {
  def this(typeSpecifier: TypeSpecifier) = this(s"${typeSpecifier.source()}")
  def this(IDENTIFIER: Identifier) = this(s"${IDENTIFIER.source()}")
  def this(FIELD_SELECTION: FieldSelection) = this(s"${FIELD_SELECTION.source()}")
  def source() = _source
}

class UnaryExpression private (_source: String) {
  def this(postfixExpression: PostfixExpression) = this(s"${postfixExpression.source()}")
  def this(INC_OP: IncOp, unaryExpression: UnaryExpression) = this(s"++ ${unaryExpression.source()}")
  def this(unaryExpression: UnaryExpression) = this(s"-- ${unaryExpression.source()}")
  def this(unaryOperator: UnaryOperator, unaryExpression: UnaryExpression) =
    this(s"${unaryOperator.source()}${unaryExpression.source()}")
  def source() = _source
}

class UnaryOperator private (_source: String) {
  def this(PLUS: Plus) = this(s"+ ")
  def this(DASH: Dash) = this(s"- ")
  def this(BANG: Bang) = this(s"! ")
  def this(TILDE: Tilde) = this(s"~ ")
  def source() = _source
}

class MultiplicativeExpression private (_source: String) {
  def this(unaryExpression: UnaryExpression) = this(s"${unaryExpression.source()}")
  def this(multiplicativeExpression: MultiplicativeExpression, unaryExpression: UnaryExpression) =
    this(s"${multiplicativeExpression.source()}* ${unaryExpression.source()}")
  def this(multiplicativeExpression: MultiplicativeExpression, SLASH: Slash, unaryExpression: UnaryExpression) =
    this(s"${multiplicativeExpression.source()}/ ${unaryExpression.source()}")
  def this(multiplicativeExpression: MultiplicativeExpression, PERCENT: Percent, unaryExpression: UnaryExpression) =
    this(s"${multiplicativeExpression.source()}% ${unaryExpression.source()}")
  def source() = _source
}

class AdditiveExpression private (_source: String) {
  def this(multiplicativeExpression: MultiplicativeExpression) = this(s"${multiplicativeExpression.source()}")
  def this(additiveExpression: AdditiveExpression, PLUS: Plus, multiplicativeExpression: MultiplicativeExpression) =
    this(s"${additiveExpression.source()}+ ${multiplicativeExpression.source()}")
  def this(additiveExpression: AdditiveExpression, multiplicativeExpression: MultiplicativeExpression) =
    this(s"${additiveExpression.source()}- ${multiplicativeExpression.source()}")
  def source() = _source
}

class ShiftExpression private (_source: String) {
  def this(additiveExpression: AdditiveExpression) = this(s"${additiveExpression.source()}")
  def this(shiftExpression: ShiftExpression, additiveExpression: AdditiveExpression) =
    this(s"${shiftExpression.source()}<< ${additiveExpression.source()}")
  def this(shiftExpression: ShiftExpression, RIGHT_OP: RightOp, additiveExpression: AdditiveExpression) =
    this(s"${shiftExpression.source()}>> ${additiveExpression.source()}")
  def source() = _source
}

class RelationalExpression private (_source: String) {
  def this(shiftExpression: ShiftExpression) = this(s"${shiftExpression.source()}")
  def this(relationalExpression: RelationalExpression, LEFT_ANGLE: LeftAngle, shiftExpression: ShiftExpression) =
    this(s"${relationalExpression.source()}< ${shiftExpression.source()}")
  def this(relationalExpression: RelationalExpression, RIGHT_ANGLE: RightAngle, shiftExpression: ShiftExpression) =
    this(s"${relationalExpression.source()}> ${shiftExpression.source()}")
  def this(relationalExpression: RelationalExpression, shiftExpression: ShiftExpression) =
    this(s"${relationalExpression.source()}<= ${shiftExpression.source()}")
  def this(relationalExpression: RelationalExpression, GE_OP: GeOp, shiftExpression: ShiftExpression) =
    this(s"${relationalExpression.source()}>= ${shiftExpression.source()}")
  def source() = _source
}

class EqualityExpression private (_source: String) {
  def this(relationalExpression: RelationalExpression) = this(s"${relationalExpression.source()}")
  def this(equalityExpression: EqualityExpression, relationalExpression: RelationalExpression) =
    this(s"${equalityExpression.source()}== ${relationalExpression.source()}")
  def this(equalityExpression: EqualityExpression, NE_OP: NeOp, relationalExpression: RelationalExpression) =
    this(s"${equalityExpression.source()}!= ${relationalExpression.source()}")
  def source() = _source
}

class AndExpression private (_source: String) {
  def this(equalityExpression: EqualityExpression) = this(s"${equalityExpression.source()}")
  def this(andExpression: AndExpression, equalityExpression: EqualityExpression) =
    this(s"${andExpression.source()}& ${equalityExpression.source()}")
  def source() = _source
}

class ExclusiveOrExpression private (_source: String) {
  def this(andExpression: AndExpression) = this(s"${andExpression.source()}")
  def this(exclusiveOrExpression: ExclusiveOrExpression, andExpression: AndExpression) =
    this(s"${exclusiveOrExpression.source()}^ ${andExpression.source()}")
  def source() = _source
}

class InclusiveOrExpression private (_source: String) {
  def this(exclusiveOrExpression: ExclusiveOrExpression) = this(s"${exclusiveOrExpression.source()}")
  def this(inclusiveOrExpression: InclusiveOrExpression, exclusiveOrExpression: ExclusiveOrExpression) =
    this(s"${inclusiveOrExpression.source()}| ${exclusiveOrExpression.source()}")
  def source() = _source
}

class LogicalAndExpression private (_source: String) {
  def this(inclusiveOrExpression: InclusiveOrExpression) = this(s"${inclusiveOrExpression.source()}")
  def this(logicalAndExpression: LogicalAndExpression, inclusiveOrExpression: InclusiveOrExpression) =
    this(s"${logicalAndExpression.source()}&& ${inclusiveOrExpression.source()}")
  def source() = _source
}

class LogicalXorExpression private (_source: String) {
  def this(logicalAndExpression: LogicalAndExpression) = this(s"${logicalAndExpression.source()}")
  def this(logicalXorExpression: LogicalXorExpression, logicalAndExpression: LogicalAndExpression) =
    this(s"${logicalXorExpression.source()}^^ ${logicalAndExpression.source()}")
  def source() = _source
}

class LogicalOrExpression private (_source: String) {
  def this(logicalXorExpression: LogicalXorExpression) = this(s"${logicalXorExpression.source()}")
  def this(logicalOrExpression: LogicalOrExpression, logicalXorExpression: LogicalXorExpression) =
    this(s"${logicalOrExpression.source()}|| ${logicalXorExpression.source()}")
  def source() = _source
}

class ConditionalExpression private (_source: String) {
  def this(logicalOrExpression: LogicalOrExpression) = this(s"${logicalOrExpression.source()}")
  def this(logicalOrExpression: LogicalOrExpression,
           expression: Expression,
           assignmentExpression: AssignmentExpression) =
    this(s"${logicalOrExpression.source()}? ${expression.source()}: ${assignmentExpression.source()}")
  def source() = _source
}

class AssignmentExpression private (_source: String) {
  def this(conditionalExpression: ConditionalExpression) = this(s"${conditionalExpression.source()}")
  def this(unaryExpression: UnaryExpression,
           assignmentOperator: AssignmentOperator,
           assignmentExpression: AssignmentExpression) =
    this(s"${unaryExpression.source()}${assignmentOperator.source()}${assignmentExpression.source()}")
  def source() = _source
}

class AssignmentOperator private (_source: String) {
  def this(EQUAL: Equal) = this(s"= ")
  def this(MUL_ASSIGN: MulAssign) = this(s"*= ")
  def this(DIV_ASSIGN: DivAssign) = this(s"/= ")
  def this(MOD_ASSIGN: ModAssign) = this(s"%= ")
  def this(ADD_ASSIGN: AddAssign) = this(s"+= ")
  def this(SUB_ASSIGN: SubAssign) = this(s"-= ")
  def this(LEFT_ASSIGN: LeftAssign) = this(s"<<= ")
  def this(RIGHT_ASSIGN: RightAssign) = this(s">>= ")
  def this(AND_ASSIGN: AndAssign) = this(s"&&= ")
  def this(XOR_ASSIGN: XorAssign) = this(s"^^= ")
  def this(OR_ASSIGN: OrAssign) = this(s"||= ")
  def source() = _source
}

class Expression private (_source: String) {
  def this(assignmentExpression: AssignmentExpression) = this(s"${assignmentExpression.source()}")
  def this(expression: Expression, assignmentExpression: AssignmentExpression) =
    this(s"${expression.source()}, ${assignmentExpression.source()}")
  def source() = _source
}

class ConstantExpression private (_source: String) {
  def this(conditionalExpression: ConditionalExpression) = this(s"${conditionalExpression.source()}")
  def source() = _source
}

class Declaration private (_source: String) {
  def this(functionPrototype: FunctionPrototype) = this(s"${functionPrototype.source()}; ")
  def this(initDeclaratorList: InitDeclaratorList) = this(s"${initDeclaratorList.source()}; ")
  def this(precisionQualifier: PrecisionQualifier, typeSpecifierNoPrec: TypeSpecifierNoPrec) =
    this(s"precision ${precisionQualifier.source()}${typeSpecifierNoPrec.source()}; ")
  def this(typeQualifier: TypeQualifier, IDENTIFIER: Identifier, structDeclarationList: StructDeclarationList) =
    this(s"${typeQualifier.source()}${IDENTIFIER.source()}{ ${structDeclarationList.source()}} ; ")
  def this(typeQualifier: TypeQualifier,
           IDENTIFIER: Identifier,
           structDeclarationList: StructDeclarationList,
           IDENTIFIER2: Identifier) =
    this(
      s"${typeQualifier.source()}${IDENTIFIER.source()}{ ${structDeclarationList.source()}} ${IDENTIFIER2.source()}; "
    )
  def this(typeQualifier: TypeQualifier,
           IDENTIFIER: Identifier,
           structDeclarationList: StructDeclarationList,
           IDENTIFIER2: Identifier,
           constantExpression: ConstantExpression) =
    this(s"${typeQualifier.source()}${IDENTIFIER.source()}{ ${structDeclarationList.source()}} ${IDENTIFIER2
      .source()}[ ${constantExpression.source()}] ; ")
  def this(typeQualifier: TypeQualifier) = this(s"${typeQualifier.source()}; ")
  def source() = _source
}

class FunctionPrototype private (_source: String) {
  def this(functionDeclarator: FunctionDeclarator) = this(s"${functionDeclarator.source()}) ")
  def source() = _source
}

class FunctionDeclarator private (_source: String) {
  def this(functionHeader: FunctionHeader) = this(s"${functionHeader.source()}")
  def this(functionHeaderWithParameters: FunctionHeaderWithParameters) =
    this(s"${functionHeaderWithParameters.source()}")
  def source() = _source
}

class FunctionHeaderWithParameters private (_source: String) {
  def this(functionHeader: FunctionHeader, parameterDeclaration: ParameterDeclaration) =
    this(s"${functionHeader.source()}${parameterDeclaration.source()}")
  def this(functionHeaderWithParameters: FunctionHeaderWithParameters, parameterDeclaration: ParameterDeclaration) =
    this(s"${functionHeaderWithParameters.source()}, ${parameterDeclaration.source()}")
  def source() = _source
}

class FunctionHeader private (_source: String) {
  def this(fullySpecifiedType: FullySpecifiedType, IDENTIFIER: Identifier) =
    this(s"${fullySpecifiedType.source()}${IDENTIFIER.source()}( ")
  def source() = _source
}

class ParameterDeclarator private (_source: String) {
  def this(typeSpecifier: TypeSpecifier, IDENTIFIER: Identifier) =
    this(s"${typeSpecifier.source()}${IDENTIFIER.source()}")
  def this(typeSpecifier: TypeSpecifier, IDENTIFIER: Identifier, constantExpression: ConstantExpression) =
    this(s"${typeSpecifier.source()}${IDENTIFIER.source()}[ ${constantExpression.source()}] ")
  def source() = _source
}

class ParameterDeclaration private (_source: String) {
  def this(parameterTypeQualifier: ParameterTypeQualifier,
           parameterQualifier: ParameterQualifier,
           parameterDeclarator: ParameterDeclarator) =
    this(s"${parameterTypeQualifier.source()}${parameterQualifier.source()}${parameterDeclarator.source()}")
  def this(parameterQualifier: ParameterQualifier, parameterDeclarator: ParameterDeclarator) =
    this(s"${parameterQualifier.source()}${parameterDeclarator.source()}")
  def this(parameterTypeQualifier: ParameterTypeQualifier,
           parameterQualifier: ParameterQualifier,
           parameterTypeSpecifier: ParameterTypeSpecifier) =
    this(s"${parameterTypeQualifier.source()}${parameterQualifier.source()}${parameterTypeSpecifier.source()}")
  def this(parameterQualifier: ParameterQualifier, parameterTypeSpecifier: ParameterTypeSpecifier) =
    this(s"${parameterQualifier.source()}${parameterTypeSpecifier.source()}")
  def source() = _source
}

class ParameterQualifier private (_source: String) {
  def this() = this(s"")
  def this(IN: In) = this(s"in ")
  def this(OUT: Out) = this(s"out ")
  def this(INOUT: Inout) = this(s"inout ")
  def source() = _source
}

class ParameterTypeSpecifier private (_source: String) {
  def this(typeSpecifier: TypeSpecifier) = this(s"${typeSpecifier.source()}")
  def source() = _source
}

class InitDeclaratorList private (_source: String) {
  def this(singleDeclaration: SingleDeclaration) = this(s"${singleDeclaration.source()}")
  def this(initDeclaratorList: InitDeclaratorList, IDENTIFIER: Identifier) =
    this(s"${initDeclaratorList.source()}, ${IDENTIFIER.source()}")
  def this(initDeclaratorList: InitDeclaratorList,
           IDENTIFIER: Identifier,
           LEFT_BRACKET: LeftBracket,
           constantExpression: ConstantExpression) =
    this(s"${initDeclaratorList.source()}, ${IDENTIFIER.source()}[ ${constantExpression.source()}] ")
  def this(initDeclaratorList: InitDeclaratorList,
           IDENTIFIER: Identifier,
           LEFT_BRACKET: LeftBracket,
           initializer: Initializer) =
    this(s"${initDeclaratorList.source()}, ${IDENTIFIER.source()}[ ] = ${initializer.source()}")
  def this(initDeclaratorList: InitDeclaratorList,
           IDENTIFIER: Identifier,
           LEFT_BRACKET: LeftBracket,
           constantExpression: ConstantExpression,
           initializer: Initializer) =
    this(
      s"${initDeclaratorList.source()}, ${IDENTIFIER.source()}[ ${constantExpression.source()}] = ${initializer.source()}"
    )
  def this(initDeclaratorList: InitDeclaratorList, IDENTIFIER: Identifier, initializer: Initializer) =
    this(s"${initDeclaratorList.source()}, ${IDENTIFIER.source()}= ${initializer.source()}")
  def source() = _source
}

class SingleDeclaration private (_source: String) {
  def this(fullySpecifiedType: FullySpecifiedType) = this(s"${fullySpecifiedType.source()}")
  def this(fullySpecifiedType: FullySpecifiedType, IDENTIFIER: Identifier) =
    this(s"${fullySpecifiedType.source()}${IDENTIFIER.source()}")
  def this(fullySpecifiedType: FullySpecifiedType,
           IDENTIFIER: Identifier,
           LEFT_BRACKET: LeftBracket,
           constantExpression: ConstantExpression) =
    this(s"${fullySpecifiedType.source()}${IDENTIFIER.source()}[ ${constantExpression.source()}] ")
  def this(fullySpecifiedType: FullySpecifiedType,
           IDENTIFIER: Identifier,
           LEFT_BRACKET: LeftBracket,
           initializer: Initializer) =
    this(s"${fullySpecifiedType.source()}${IDENTIFIER.source()}[ ] = ${initializer.source()}")
  def this(fullySpecifiedType: FullySpecifiedType,
           IDENTIFIER: Identifier,
           LEFT_BRACKET: LeftBracket,
           constantExpression: ConstantExpression,
           initializer: Initializer) =
    this(
      s"${fullySpecifiedType.source()}${IDENTIFIER.source()}[ ${constantExpression.source()}] = ${initializer.source()}"
    )
  def this(fullySpecifiedType: FullySpecifiedType, IDENTIFIER: Identifier, initializer: Initializer) =
    this(s"${fullySpecifiedType.source()}${IDENTIFIER.source()}= ${initializer.source()}")
  def this(IDENTIFIER: Identifier) = this(s"invariant ${IDENTIFIER.source()}")
  def source() = _source
}

class FullySpecifiedType private (_source: String) {
  def this(typeSpecifier: TypeSpecifier) = this(s"${typeSpecifier.source()}")
  def this(typeQualifier: TypeQualifier, typeSpecifier: TypeSpecifier) =
    this(s"${typeQualifier.source()}${typeSpecifier.source()}")
  def source() = _source
}

class InvariantQualifier private (_source: String) {
  def this(INVARIANT: Invariant) = this(s"invariant ")
  def source() = _source
}

class InterpolationQualifier private (_source: String) {
  def this(SMOOTH: Smooth) = this(s"smooth ")
  def this(FLAT: Flat) = this(s"flat ")
  def source() = _source
}

class LayoutQualifier private (_source: String) {
  def this(layoutQualifierIdList: LayoutQualifierIdList) = this(s"layout ( ${layoutQualifierIdList.source()}) ")
  def source() = _source
}

class LayoutQualifierIdList private (_source: String) {
  def this(layoutQualifierId: LayoutQualifierId) = this(s"${layoutQualifierId.source()}")
  def this(layoutQualifierIdList: LayoutQualifierIdList, layoutQualifierId: LayoutQualifierId) =
    this(s"${layoutQualifierIdList.source()}, ${layoutQualifierId.source()}")
  def source() = _source
}

class LayoutQualifierId private (_source: String) {
  def this(IDENTIFIER: Identifier) = this(s"${IDENTIFIER.source()}")
  def this(IDENTIFIER: Identifier, INTCONSTANT: Intconstant) = this(s"${IDENTIFIER.source()}= ${INTCONSTANT.source()}")
  def this(IDENTIFIER: Identifier, UINTCONSTANT: Uintconstant) =
    this(s"${IDENTIFIER.source()}= ${UINTCONSTANT.source()}")
  def source() = _source
}

class ParameterTypeQualifier private (_source: String) {
  def this(CONST: Const) = this(s"const ")
  def source() = _source
}

class TypeQualifier private (_source: String) {
  def this(storageQualifier: StorageQualifier) = this(s"${storageQualifier.source()}")
  def this(layoutQualifier: LayoutQualifier) = this(s"${layoutQualifier.source()}")
  def this(layoutQualifier: LayoutQualifier, storageQualifier: StorageQualifier) =
    this(s"${layoutQualifier.source()}${storageQualifier.source()}")
  def this(interpolationQualifier: InterpolationQualifier, storageQualifier: StorageQualifier) =
    this(s"${interpolationQualifier.source()}${storageQualifier.source()}")
  def this(interpolationQualifier: InterpolationQualifier) = this(s"${interpolationQualifier.source()}")
  def this(invariantQualifier: InvariantQualifier, storageQualifier: StorageQualifier) =
    this(s"${invariantQualifier.source()}${storageQualifier.source()}")
  def this(invariantQualifier: InvariantQualifier,
           interpolationQualifier: InterpolationQualifier,
           storageQualifier: StorageQualifier) =
    this(s"${invariantQualifier.source()}${interpolationQualifier.source()}${storageQualifier.source()}")
  def source() = _source
}

class StorageQualifier private (_source: String) {
  def this(CONST: Const) = this(s"const ")
  def this(IN: In) = this(s"in ")
  def this(OUT: Out) = this(s"out ")
  def this(CENTROID: Centroid, IN: In) = this(s"centroid in ")
  def this(CENTROID: Centroid, OUT: Out) = this(s"centroid out ")
  def this(UNIFORM: Uniform) = this(s"uniform ")
  def source() = _source
}

class TypeSpecifier private (_source: String) {
  def this(typeSpecifierNoPrec: TypeSpecifierNoPrec) = this(s"${typeSpecifierNoPrec.source()}")
  def this(precisionQualifier: PrecisionQualifier, typeSpecifierNoPrec: TypeSpecifierNoPrec) =
    this(s"${precisionQualifier.source()}${typeSpecifierNoPrec.source()}")
  def source() = _source
}

class TypeSpecifierNoPrec private (_source: String) {
  def this(typeSpecifierNonarray: TypeSpecifierNonarray) = this(s"${typeSpecifierNonarray.source()}")
  def this(typeSpecifierNonarray: TypeSpecifierNonarray, LEFT_BRACKET: LeftBracket) =
    this(s"${typeSpecifierNonarray.source()}[ ] ")
  def this(typeSpecifierNonarray: TypeSpecifierNonarray,
           LEFT_BRACKET: LeftBracket,
           constantExpression: ConstantExpression) =
    this(s"${typeSpecifierNonarray.source()}[ ${constantExpression.source()}] ")
  def source() = _source
}

class TypeSpecifierNonarray private (_source: String) {
  def this(VOID: Void) = this(s"void ")
  def this(FLOAT: Float) = this(s"float ")
  def this(INT: Int) = this(s"int ")
  def this(UINT: Uint) = this(s"uint ")
  def this(BOOL: Bool) = this(s"bool ")
  def this(VEC2: Vec2) = this(s"vec2 ")
  def this(VEC3: Vec3) = this(s"vec3 ")
  def this(VEC4: Vec4) = this(s"vec4 ")
  def this(BVEC2: Bvec2) = this(s"bvec2 ")
  def this(BVEC3: Bvec3) = this(s"bvec3 ")
  def this(BVEC4: Bvec4) = this(s"bvec4 ")
  def this(IVEC2: Ivec2) = this(s"ivec2 ")
  def this(IVEC3: Ivec3) = this(s"ivec3 ")
  def this(IVEC4: Ivec4) = this(s"ivec4 ")
  def this(UVEC2: Uvec2) = this(s"uvec2 ")
  def this(UVEC3: Uvec3) = this(s"uvec3 ")
  def this(UVEC4: Uvec4) = this(s"uvec4 ")
  def this(MAT2: Mat2) = this(s"mat2 ")
  def this(MAT3: Mat3) = this(s"mat3 ")
  def this(MAT4: Mat4) = this(s"mat4 ")
  def this(MAT2X2: Mat2x2) = this(s"mat2x2 ")
  def this(MAT2X3: Mat2x3) = this(s"mat2x3 ")
  def this(MAT2X4: Mat2x4) = this(s"mat2x4 ")
  def this(MAT3X2: Mat3x2) = this(s"mat3x2 ")
  def this(MAT3X3: Mat3x3) = this(s"mat3x3 ")
  def this(MAT3X4: Mat3x4) = this(s"mat3x4 ")
  def this(MAT4X2: Mat4x2) = this(s"mat4x2 ")
  def this(MAT4X3: Mat4x3) = this(s"mat4x3 ")
  def this(MAT4X4: Mat4x4) = this(s"mat4x4 ")
  def this(SAMPLER2D: Sampler2D) = this(s"sampler2D ")
  def this(SAMPLER3D: Sampler3D) = this(s"sampler3D ")
  def this(SAMPLERCUBE: Samplercube) = this(s"samplerCube ")
  def this(SAMPLER2DSHADOW: Sampler2Dshadow) = this(s"sampler2DShadow ")
  def this(SAMPLERCUBESHADOW: Samplercubeshadow) = this(s"samplerCubeShadow ")
  def this(SAMPLER2DARRAY: Sampler2DArray) = this(s"sampler2DArray ")
  def this(SAMPLER2DARRAYSHADOW: Sampler2DArrayshadow) = this(s"sampler2DArrayShadow ")
  def this(ISAMPLER2D: ISampler2D) = this(s"isampler2D ")
  def this(ISAMPLER3D: ISampler3D) = this(s"isampler3D ")
  def this(ISAMPLERCUBE: ISamplercube) = this(s"isamplerCube ")
  def this(ISAMPLER2DARRAY: ISampler2DArray) = this(s"isampler2DArray ")
  def this(USAMPLER2D: USampler2D) = this(s"usampler2D ")
  def this(USAMPLER3D: USampler3D) = this(s"usampler3D ")
  def this(USAMPLERCUBE: USamplercube) = this(s"usamplerCube ")
  def this(USAMPLER2DARRAY: USampler2DArray) = this(s"usampler2DArray ")
  def this(structSpecifier: StructSpecifier) = this(s"${structSpecifier.source()}")
  def this(TYPE_NAME: TypeName) = this(s"${TYPE_NAME.source()}")
  def source() = _source
}

class PrecisionQualifier private (_source: String) {
  def this(HIGH_PRECISION: HighPrecision) = this(s"highp ")
  def this(MEDIUM_PRECISION: MediumPrecision) = this(s"mediump ")
  def this(LOW_PRECISION: LowPrecision) = this(s"lowp ")
  def source() = _source
}

class StructSpecifier private (_source: String) {
  def this(IDENTIFIER: Identifier, structDeclarationList: StructDeclarationList) =
    this(s"struct ${IDENTIFIER.source()}{ ${structDeclarationList.source()}} ")
  def this(structDeclarationList: StructDeclarationList) = this(s"struct { ${structDeclarationList.source()}} ")
  def source() = _source
}

class StructDeclarationList private (_source: String) {
  def this(structDeclaration: StructDeclaration) = this(s"${structDeclaration.source()}")
  def this(structDeclarationList: StructDeclarationList, structDeclaration: StructDeclaration) =
    this(s"${structDeclarationList.source()}${structDeclaration.source()}")
  def source() = _source
}

class StructDeclaration private (_source: String) {
  def this(typeSpecifier: TypeSpecifier, structDeclaratorList: StructDeclaratorList) =
    this(s"${typeSpecifier.source()}${structDeclaratorList.source()}; ")
  def this(typeQualifier: TypeQualifier, typeSpecifier: TypeSpecifier, structDeclaratorList: StructDeclaratorList) =
    this(s"${typeQualifier.source()}${typeSpecifier.source()}${structDeclaratorList.source()}; ")
  def source() = _source
}

class StructDeclaratorList private (_source: String) {
  def this(structDeclarator: StructDeclarator) = this(s"${structDeclarator.source()}")
  def this(structDeclaratorList: StructDeclaratorList, structDeclarator: StructDeclarator) =
    this(s"${structDeclaratorList.source()}, ${structDeclarator.source()}")
  def source() = _source
}

class StructDeclarator private (_source: String) {
  def this(IDENTIFIER: Identifier) = this(s"${IDENTIFIER.source()}")
  def this(IDENTIFIER: Identifier, LEFT_BRACKET: LeftBracket) = this(s"${IDENTIFIER.source()}[ ] ")
  def this(IDENTIFIER: Identifier, LEFT_BRACKET: LeftBracket, constantExpression: ConstantExpression) =
    this(s"${IDENTIFIER.source()}[ ${constantExpression.source()}] ")
  def source() = _source
}

class Initializer private (_source: String) {
  def this(assignmentExpression: AssignmentExpression) = this(s"${assignmentExpression.source()}")
  def source() = _source
}

class DeclarationStatement private (_source: String) {
  def this(declaration: Declaration) = this(s"${declaration.source()}")
  def source() = _source
}

class Statement private (_source: String) {
  def this(compoundStatementWithScope: CompoundStatementWithScope) = this(s"${compoundStatementWithScope.source()}")
  def this(simpleStatement: SimpleStatement) = this(s"${simpleStatement.source()}")
  def source() = _source
}

class StatementNoNewScope private (_source: String) {
  def this(compoundStatementNoNewScope: CompoundStatementNoNewScope) = this(s"${compoundStatementNoNewScope.source()}")
  def this(simpleStatement: SimpleStatement) = this(s"${simpleStatement.source()}")
  def source() = _source
}

class StatementWithScope private (_source: String) {
  def this(compoundStatementNoNewScope: CompoundStatementNoNewScope) = this(s"${compoundStatementNoNewScope.source()}")
  def this(simpleStatement: SimpleStatement) = this(s"${simpleStatement.source()}")
  def source() = _source
}

class SimpleStatement private (_source: String) {
  def this(declarationStatement: DeclarationStatement) = this(s"${declarationStatement.source()}")
  def this(expressionStatement: ExpressionStatement) = this(s"${expressionStatement.source()}")
  def this(selectionStatement: SelectionStatement) = this(s"${selectionStatement.source()}")
  def this(switchStatement: SwitchStatement) = this(s"${switchStatement.source()}")
  def this(caseLabel: CaseLabel) = this(s"${caseLabel.source()}")
  def this(iterationStatement: IterationStatement) = this(s"${iterationStatement.source()}")
  def this(jumpStatement: JumpStatement) = this(s"${jumpStatement.source()}")
  def source() = _source
}

class CompoundStatementWithScope private (_source: String) {
  def this(RIGHT_BRACE: RightBrace) = this(s"{ } ")
  def this(statementList: StatementList, RIGHT_BRACE: RightBrace) = this(s"{ ${statementList.source()}} ")
  def source() = _source
}

class CompoundStatementNoNewScope private (_source: String) {
  def this(RIGHT_BRACE: RightBrace) = this(s"{ } ")
  def this(statementList: StatementList, RIGHT_BRACE: RightBrace) = this(s"{ ${statementList.source()}} ")
  def source() = _source
}

class StatementList private (_source: String) {
  def this(statement: Statement) = this(s"${statement.source()}")
  def this(statementList: StatementList, statement: Statement) = this(s"${statementList.source()}${statement.source()}")
  def source() = _source
}

class ExpressionStatement private (_source: String) {
  def this() = this(s"; ")
  def this(expression: Expression) = this(s"${expression.source()}; ")
  def source() = _source
}

class SelectionStatement private (_source: String) {
  def this(expression: Expression, selectionRestStatement: SelectionRestStatement) =
    this(s"if ( ${expression.source()}) ${selectionRestStatement.source()}")
  def source() = _source
}

class SelectionRestStatement private (_source: String) {
  def this(statementWithScope: StatementWithScope, statementWithScope2: StatementWithScope) =
    this(s"${statementWithScope.source()}else ${statementWithScope2.source()}")
  def this(statementWithScope: StatementWithScope) = this(s"${statementWithScope.source()}")
  def source() = _source
}

class Condition private (_source: String) {
  def this(expression: Expression) = this(s"${expression.source()}")
  def this(fullySpecifiedType: FullySpecifiedType, IDENTIFIER: Identifier, initializer: Initializer) =
    this(s"${fullySpecifiedType.source()}${IDENTIFIER.source()}= ${initializer.source()}")
  def source() = _source
}

class SwitchStatement private (_source: String) {
  def this(expression: Expression, switchStatementList: SwitchStatementList) =
    this(s"switch ( ${expression.source()}) { ${switchStatementList.source()}} ")
  def source() = _source
}

class SwitchStatementList private (_source: String) {
  def this() = this(s"")
  def this(statementList: StatementList) = this(s"${statementList.source()}")
  def source() = _source
}

class CaseLabel private (_source: String) {
  def this(expression: Expression) = this(s"case ${expression.source()}: ")
  def this(DEFAULT: Default) = this(s"default : ")
  def source() = _source
}

class IterationStatement private (_source: String) {
  def this(condition: Condition, statementNoNewScope: StatementNoNewScope) =
    this(s"while ( ${condition.source()}) ${statementNoNewScope.source()}")
  def this(statementWithScope: StatementWithScope, expression: Expression) =
    this(s"do ${statementWithScope.source()}while ( ${expression.source()}) ; ")
  def this(forInitStatement: ForInitStatement,
           forRestStatement: ForRestStatement,
           statementNoNewScope: StatementNoNewScope) =
    this(s"for ( ${forInitStatement.source()}${forRestStatement.source()}) ${statementNoNewScope.source()}")
  def source() = _source
}

class ForInitStatement private (_source: String) {
  def this(expressionStatement: ExpressionStatement) = this(s"${expressionStatement.source()}")
  def this(declarationStatement: DeclarationStatement) = this(s"${declarationStatement.source()}")
  def source() = _source
}

class Conditionopt private (_source: String) {
  def this(condition: Condition) = this(s"${condition.source()}")
  def this() = this(s"")
  def source() = _source
}

class ForRestStatement private (_source: String) {
  def this(conditionopt: Conditionopt) = this(s"${conditionopt.source()}; ")
  def this(conditionopt: Conditionopt, expression: Expression) =
    this(s"${conditionopt.source()}; ${expression.source()}")
  def source() = _source
}

class JumpStatement private (_source: String) {
  def this(CONTINUE: Continue) = this(s"continue ; ")
  def this(BREAK: Break) = this(s"break ; ")
  def this(RETURN: Return) = this(s"return ; ")
  def this(RETURN: Return, expression: Expression) = this(s"return ${expression.source()}; ")
  def this(DISCARD: Discard) = this(s"discard ; ")
  def source() = _source
}

class TranslationUnit private (_source: String) {
  def this(externalDeclaration: ExternalDeclaration) = this(s"${externalDeclaration.source()}")
  def this(translationUnit: TranslationUnit, externalDeclaration: ExternalDeclaration) =
    this(s"${translationUnit.source()}${externalDeclaration.source()}")
  def source() = _source
}

class ExternalDeclaration private (_source: String) {
  def this(functionDefinition: FunctionDefinition) = this(s"${functionDefinition.source()}")
  def this(declaration: Declaration) = this(s"${declaration.source()}")
  def source() = _source
}

class FunctionDefinition private (_source: String) {
  def this(functionPrototype: FunctionPrototype, compoundStatementNoNewScope: CompoundStatementNoNewScope) =
    this(s"${functionPrototype.source()}${compoundStatementNoNewScope.source()}")
  def source() = _source
}
