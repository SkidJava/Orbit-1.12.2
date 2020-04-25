/*
 * Javassist, a Java-bytecode translator toolkit.
 * Copyright (C) 2004 Bill Burke. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License.  Alternatively, the contents of this file may be used under
 * the terms of the GNU Lesser General Public License Version 2.1 or later,
 * or the Apache License Version 2.0.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 */

package javassist.bytecode.annotation;

/**
 * Visitor for traversing member values included in an annotation.
 *
 * @see MemberValue#accept(MemberValueVisitor)
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 */
public interface MemberValueVisitor
{
    void visitAnnotationMemberValue(AnnotationMemberValue node);
    void visitArrayMemberValue(ArrayMemberValue node);
    void visitBooleanMemberValue(BooleanMemberValue node);
    void visitByteMemberValue(ByteMemberValue node);
    void visitCharMemberValue(CharMemberValue node);
    void visitDoubleMemberValue(DoubleMemberValue node);
    void visitEnumMemberValue(EnumMemberValue node);
    void visitFloatMemberValue(FloatMemberValue node);
    void visitIntegerMemberValue(IntegerMemberValue node);
    void visitLongMemberValue(LongMemberValue node);
    void visitShortMemberValue(ShortMemberValue node);
    void visitStringMemberValue(StringMemberValue node);
    void visitClassMemberValue(ClassMemberValue node);
}
