/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.impl.heuristic.selector.move.composite;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.optaplanner.core.impl.heuristic.selector.SelectorTestUtils;
import org.optaplanner.core.impl.heuristic.selector.entity.EntitySelector;
import org.optaplanner.core.impl.heuristic.selector.entity.mimic.MimicRecordingEntitySelector;
import org.optaplanner.core.impl.heuristic.selector.entity.mimic.MimicReplayingEntitySelector;
import org.optaplanner.core.impl.heuristic.selector.move.MoveSelector;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMoveSelector;
import org.optaplanner.core.impl.heuristic.selector.value.ValueSelector;
import org.optaplanner.core.impl.move.DummyMove;
import org.optaplanner.core.impl.phase.AbstractSolverPhaseScope;
import org.optaplanner.core.impl.phase.step.AbstractStepScope;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;
import org.optaplanner.core.impl.testdata.domain.TestdataValue;
import org.optaplanner.core.impl.testdata.domain.multivar.TestdataMultiVarEntity;

import static org.mockito.Mockito.*;
import static org.optaplanner.core.impl.testdata.util.PlannerAssert.*;

public class CartesianProductMoveSelectorTest {

    @Test
    public void originSelectionNotIgnoringEmpty() {
        originSelection(false);
    }

    @Test
    public void originSelectionIgnoringEmpty() {
        originSelection(true);
    }

    public void originSelection(boolean ignoreEmptyChildIterators) {
        ArrayList<MoveSelector> childMoveSelectorList = new ArrayList<MoveSelector>();
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("a1"), new DummyMove("a2"), new DummyMove("a3")));
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("b1"), new DummyMove("b2")));
        CartesianProductMoveSelector moveSelector = new CartesianProductMoveSelector(childMoveSelectorList,
                ignoreEmptyChildIterators, false);

        DefaultSolverScope solverScope = mock(DefaultSolverScope.class);
        moveSelector.solvingStarted(solverScope);
        AbstractSolverPhaseScope phaseScopeA = mock(AbstractSolverPhaseScope.class);
        when(phaseScopeA.getSolverScope()).thenReturn(solverScope);
        moveSelector.phaseStarted(phaseScopeA);
        AbstractStepScope stepScopeA1 = mock(AbstractStepScope.class);
        when(stepScopeA1.getPhaseScope()).thenReturn(phaseScopeA);
        moveSelector.stepStarted(stepScopeA1);

        assertAllCodesOfMoveSelector(moveSelector,
                "a1+b1", "a1+b2",
                "a2+b1", "a2+b2",
                "a3+b1", "a3+b2");

        moveSelector.stepEnded(stepScopeA1);
        moveSelector.phaseEnded(phaseScopeA);
        moveSelector.solvingEnded(solverScope);

        verifySolverPhaseLifecycle(childMoveSelectorList.get(0), 1, 1, 1);
        verifySolverPhaseLifecycle(childMoveSelectorList.get(1), 1, 1, 1);
    }

    @Test
    public void emptyOriginSelectionNotIgnoringEmpty() {
        emptyOriginSelection(false);
    }

    @Test
    public void emptyOriginSelectionIgnoringEmpty() {
        emptyOriginSelection(true);
    }

    public void emptyOriginSelection(boolean ignoreEmptyChildIterators) {
        ArrayList<MoveSelector> childMoveSelectorList = new ArrayList<MoveSelector>();
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
        new DummyMove("a1"), new DummyMove("a2"), new DummyMove("a3"))); // One side is not empty
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class));
        CartesianProductMoveSelector moveSelector = new CartesianProductMoveSelector(childMoveSelectorList,
                ignoreEmptyChildIterators, false);

        DefaultSolverScope solverScope = mock(DefaultSolverScope.class);
        moveSelector.solvingStarted(solverScope);
        AbstractSolverPhaseScope phaseScopeA = mock(AbstractSolverPhaseScope.class);
        when(phaseScopeA.getSolverScope()).thenReturn(solverScope);
        moveSelector.phaseStarted(phaseScopeA);
        AbstractStepScope stepScopeA1 = mock(AbstractStepScope.class);
        when(stepScopeA1.getPhaseScope()).thenReturn(phaseScopeA);
        moveSelector.stepStarted(stepScopeA1);

        if (ignoreEmptyChildIterators) {
            assertAllCodesOfMoveSelector(moveSelector, "a1", "a2", "a3");
        } else {
            assertAllCodesOfMoveSelector(moveSelector);
        }

        moveSelector.stepEnded(stepScopeA1);
        moveSelector.phaseEnded(phaseScopeA);
        moveSelector.solvingEnded(solverScope);

        verifySolverPhaseLifecycle(childMoveSelectorList.get(0), 1, 1, 1);
        verifySolverPhaseLifecycle(childMoveSelectorList.get(1), 1, 1, 1);
    }

    @Test
    public void originSelection3ChildMoveSelectorsNotIgnoringEmpty() {
        originSelection3ChildMoveSelectors(false);
    }

    @Test
    public void originSelection3ChildMoveSelectorsIgnoringEmpty() {
        originSelection3ChildMoveSelectors(true);
    }

    public void originSelection3ChildMoveSelectors(boolean ignoreEmptyChildIterators) {
        ArrayList<MoveSelector> childMoveSelectorList = new ArrayList<MoveSelector>();
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("a1"), new DummyMove("a2")));
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("b1"), new DummyMove("b2")));
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("c1"), new DummyMove("c2")));
        CartesianProductMoveSelector moveSelector = new CartesianProductMoveSelector(childMoveSelectorList,
                ignoreEmptyChildIterators, false);

        DefaultSolverScope solverScope = mock(DefaultSolverScope.class);
        moveSelector.solvingStarted(solverScope);
        AbstractSolverPhaseScope phaseScopeA = mock(AbstractSolverPhaseScope.class);
        when(phaseScopeA.getSolverScope()).thenReturn(solverScope);
        moveSelector.phaseStarted(phaseScopeA);
        AbstractStepScope stepScopeA1 = mock(AbstractStepScope.class);
        when(stepScopeA1.getPhaseScope()).thenReturn(phaseScopeA);
        moveSelector.stepStarted(stepScopeA1);

        assertAllCodesOfMoveSelector(moveSelector,
                "a1+b1+c1", "a1+b1+c2", "a1+b2+c1", "a1+b2+c2",
                "a2+b1+c1", "a2+b1+c2", "a2+b2+c1", "a2+b2+c2");

        moveSelector.stepEnded(stepScopeA1);
        moveSelector.phaseEnded(phaseScopeA);
        moveSelector.solvingEnded(solverScope);

        verifySolverPhaseLifecycle(childMoveSelectorList.get(0), 1, 1, 1);
        verifySolverPhaseLifecycle(childMoveSelectorList.get(1), 1, 1, 1);
    }

    @Test
    public void emptyOriginSelection3ChildMoveSelectorsNotIgnoringEmpty() {
        emptyOriginSelection3ChildMoveSelectors(false);
    }

    @Test
    public void emptyOriginSelection3ChildMoveSelectorsIgnoringEmpty() {
        emptyOriginSelection3ChildMoveSelectors(true);
    }

    public void emptyOriginSelection3ChildMoveSelectors(boolean ignoreEmptyChildIterators) {
        ArrayList<MoveSelector> childMoveSelectorList = new ArrayList<MoveSelector>();
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("a1"), new DummyMove("a2")));
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class));
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("c1"), new DummyMove("c2")));
        CartesianProductMoveSelector moveSelector = new CartesianProductMoveSelector(childMoveSelectorList,
                ignoreEmptyChildIterators, false);

        DefaultSolverScope solverScope = mock(DefaultSolverScope.class);
        moveSelector.solvingStarted(solverScope);
        AbstractSolverPhaseScope phaseScopeA = mock(AbstractSolverPhaseScope.class);
        when(phaseScopeA.getSolverScope()).thenReturn(solverScope);
        moveSelector.phaseStarted(phaseScopeA);
        AbstractStepScope stepScopeA1 = mock(AbstractStepScope.class);
        when(stepScopeA1.getPhaseScope()).thenReturn(phaseScopeA);
        moveSelector.stepStarted(stepScopeA1);

        if (ignoreEmptyChildIterators) {
            assertAllCodesOfMoveSelector(moveSelector,
                    "a1+c1", "a1+c2", "a2+c1", "a2+c2");
        } else {
            assertAllCodesOfMoveSelector(moveSelector);
        }

        moveSelector.stepEnded(stepScopeA1);
        moveSelector.phaseEnded(phaseScopeA);
        moveSelector.solvingEnded(solverScope);

        verifySolverPhaseLifecycle(childMoveSelectorList.get(0), 1, 1, 1);
        verifySolverPhaseLifecycle(childMoveSelectorList.get(1), 1, 1, 1);
    }

    @Test
    public void classicRandomSelectionNotIgnoringEmpty() {
        classicRandomSelection(false);
    }

    @Test
    public void classicRandomSelectionIgnoringEmpty() {
        classicRandomSelection(true);
    }

    public void classicRandomSelection(boolean ignoreEmptyChildIterators) {
        ArrayList<MoveSelector> childMoveSelectorList = new ArrayList<MoveSelector>();
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("a1"), new DummyMove("a2"), new DummyMove("a3")));
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("b1"), new DummyMove("b2")));
        CartesianProductMoveSelector moveSelector = new CartesianProductMoveSelector(childMoveSelectorList,
                ignoreEmptyChildIterators, true);

        DefaultSolverScope solverScope = mock(DefaultSolverScope.class);
        moveSelector.solvingStarted(solverScope);
        AbstractSolverPhaseScope phaseScopeA = mock(AbstractSolverPhaseScope.class);
        when(phaseScopeA.getSolverScope()).thenReturn(solverScope);
        moveSelector.phaseStarted(phaseScopeA);
        AbstractStepScope stepScopeA1 = mock(AbstractStepScope.class);
        when(stepScopeA1.getPhaseScope()).thenReturn(phaseScopeA);
        moveSelector.stepStarted(stepScopeA1);

        assertCodesOfNeverEndingMoveSelector(moveSelector, 6, "a1+b1", "a2+b2", "a3+b1", "a1+b2", "a2+b1", "a3+b2");

        moveSelector.stepEnded(stepScopeA1);
        moveSelector.phaseEnded(phaseScopeA);
        moveSelector.solvingEnded(solverScope);

        verifySolverPhaseLifecycle(childMoveSelectorList.get(0), 1, 1, 1);
        verifySolverPhaseLifecycle(childMoveSelectorList.get(1), 1, 1, 1);
    }

    @Test
    public void emptyRandomSelectionNotIgnoringEmpty() {
        emptyRandomSelection(false);
    }

    @Test
    public void emptyRandomSelectionIgnoringEmpty() {
        emptyRandomSelection(true);
    }

    public void emptyRandomSelection(boolean ignoreEmptyChildIterators) {
        ArrayList<MoveSelector> childMoveSelectorList = new ArrayList<MoveSelector>();
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class));
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("b1"), new DummyMove("b2"))); // One side is not empty
        CartesianProductMoveSelector moveSelector = new CartesianProductMoveSelector(childMoveSelectorList,
                ignoreEmptyChildIterators, true);

        DefaultSolverScope solverScope = mock(DefaultSolverScope.class);
        moveSelector.solvingStarted(solverScope);
        AbstractSolverPhaseScope phaseScopeA = mock(AbstractSolverPhaseScope.class);
        when(phaseScopeA.getSolverScope()).thenReturn(solverScope);
        moveSelector.phaseStarted(phaseScopeA);
        AbstractStepScope stepScopeA1 = mock(AbstractStepScope.class);
        when(stepScopeA1.getPhaseScope()).thenReturn(phaseScopeA);
        moveSelector.stepStarted(stepScopeA1);

        if (ignoreEmptyChildIterators) {
            assertCodesOfNeverEndingMoveSelector(moveSelector, "b1", "b2");
        } else {
            assertEmptyNeverEndingMoveSelector(moveSelector);
        }

        moveSelector.stepEnded(stepScopeA1);
        moveSelector.phaseEnded(phaseScopeA);
        moveSelector.solvingEnded(solverScope);

        verifySolverPhaseLifecycle(childMoveSelectorList.get(0), 1, 1, 1);
        verifySolverPhaseLifecycle(childMoveSelectorList.get(1), 1, 1, 1);
    }

    @Test
    public void randomSelection3ChildMoveSelectorsNotIgnoringEmpty() {
        randomSelection3ChildMoveSelectors(false);
    }

    @Test
    public void randomSelection3ChildMoveSelectorsIgnoringEmpty() {
        randomSelection3ChildMoveSelectors(true);
    }

    public void randomSelection3ChildMoveSelectors(boolean ignoreEmptyChildIterators) {
        ArrayList<MoveSelector> childMoveSelectorList = new ArrayList<MoveSelector>();
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("a1"), new DummyMove("a2")));
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("b1"), new DummyMove("b2")));
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("c1"), new DummyMove("c2")));
        CartesianProductMoveSelector moveSelector = new CartesianProductMoveSelector(childMoveSelectorList,
                ignoreEmptyChildIterators, true);

        DefaultSolverScope solverScope = mock(DefaultSolverScope.class);
        moveSelector.solvingStarted(solverScope);
        AbstractSolverPhaseScope phaseScopeA = mock(AbstractSolverPhaseScope.class);
        when(phaseScopeA.getSolverScope()).thenReturn(solverScope);
        moveSelector.phaseStarted(phaseScopeA);
        AbstractStepScope stepScopeA1 = mock(AbstractStepScope.class);
        when(stepScopeA1.getPhaseScope()).thenReturn(phaseScopeA);
        moveSelector.stepStarted(stepScopeA1);

        assertCodesOfNeverEndingMoveSelector(moveSelector, 8L, "a1+b1+c1", "a2+b2+c2", "a1+b1+c1");

        moveSelector.stepEnded(stepScopeA1);
        moveSelector.phaseEnded(phaseScopeA);
        moveSelector.solvingEnded(solverScope);

        verifySolverPhaseLifecycle(childMoveSelectorList.get(0), 1, 1, 1);
        verifySolverPhaseLifecycle(childMoveSelectorList.get(1), 1, 1, 1);
    }

    @Test
    public void emptyRandomSelection3ChildMoveSelectorsNotIgnoringEmpty() {
        emptyRandomSelection3ChildMoveSelectors(false);
    }

    @Test
    public void emptyRandomSelection3ChildMoveSelectorsIgnoringEmpty() {
        emptyRandomSelection3ChildMoveSelectors(true);
    }

    public void emptyRandomSelection3ChildMoveSelectors(boolean ignoreEmptyChildIterators) {
        ArrayList<MoveSelector> childMoveSelectorList = new ArrayList<MoveSelector>();
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class));
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("b1"), new DummyMove("b2")));
        childMoveSelectorList.add(SelectorTestUtils.mockMoveSelector(DummyMove.class,
                new DummyMove("c1"), new DummyMove("c2"), new DummyMove("c3")));
        CartesianProductMoveSelector moveSelector = new CartesianProductMoveSelector(childMoveSelectorList,
                ignoreEmptyChildIterators, true);

        DefaultSolverScope solverScope = mock(DefaultSolverScope.class);
        moveSelector.solvingStarted(solverScope);
        AbstractSolverPhaseScope phaseScopeA = mock(AbstractSolverPhaseScope.class);
        when(phaseScopeA.getSolverScope()).thenReturn(solverScope);
        moveSelector.phaseStarted(phaseScopeA);
        AbstractStepScope stepScopeA1 = mock(AbstractStepScope.class);
        when(stepScopeA1.getPhaseScope()).thenReturn(phaseScopeA);
        moveSelector.stepStarted(stepScopeA1);

        if (ignoreEmptyChildIterators) {
            assertCodesOfNeverEndingMoveSelector(moveSelector, 6L, "b1+c1", "b2+c2", "b1+c3");
        } else {
            assertEmptyNeverEndingMoveSelector(moveSelector);
        }

        moveSelector.stepEnded(stepScopeA1);
        moveSelector.phaseEnded(phaseScopeA);
        moveSelector.solvingEnded(solverScope);

        verifySolverPhaseLifecycle(childMoveSelectorList.get(0), 1, 1, 1);
        verifySolverPhaseLifecycle(childMoveSelectorList.get(1), 1, 1, 1);
    }

    // ************************************************************************
    // Integration with mimic
    // ************************************************************************

    @Test
    public void originalMimicNotIgnoringEmpty() {
        originalMimic(false);
    }

    @Test
    public void originalMimicIgnoringEmpty() {
        originalMimic(true);
    }

    public void originalMimic(boolean ignoreEmptyChildIterators) {
        EntitySelector entitySelector = SelectorTestUtils.mockEntitySelector(TestdataMultiVarEntity.class,
                new TestdataMultiVarEntity("a"), new TestdataMultiVarEntity("b"));
        MimicRecordingEntitySelector recordingEntitySelector = new MimicRecordingEntitySelector(
                entitySelector);
        ValueSelector primaryValueSelector = SelectorTestUtils.mockValueSelector(
                TestdataMultiVarEntity.class, "primaryValue",
                new TestdataValue("1"), new TestdataValue("2"), new TestdataValue("3"));
        ValueSelector secondaryValueSelector = SelectorTestUtils.mockValueSelector(
                TestdataMultiVarEntity.class, "secondaryValue",
                new TestdataValue("8"), new TestdataValue("9"));

        List<MoveSelector> moveSelectorList = new ArrayList<MoveSelector>(2);
        moveSelectorList.add(new ChangeMoveSelector(
                recordingEntitySelector,
                primaryValueSelector,
                false));
        moveSelectorList.add(new ChangeMoveSelector(
                new MimicReplayingEntitySelector(recordingEntitySelector),
                secondaryValueSelector,
                false));
        MoveSelector moveSelector = new CartesianProductMoveSelector(moveSelectorList,
                ignoreEmptyChildIterators, false);

        DefaultSolverScope solverScope = mock(DefaultSolverScope.class);
        moveSelector.solvingStarted(solverScope);

        AbstractSolverPhaseScope phaseScopeA = mock(AbstractSolverPhaseScope.class);
        when(phaseScopeA.getSolverScope()).thenReturn(solverScope);
        moveSelector.phaseStarted(phaseScopeA);

        AbstractStepScope stepScopeA1 = mock(AbstractStepScope.class);
        when(stepScopeA1.getPhaseScope()).thenReturn(phaseScopeA);
        moveSelector.stepStarted(stepScopeA1);
        assertAllCodesOfMoveSelector(moveSelector, DO_NOT_ASSERT_SIZE,
                "a->1+a->8", "a->1+a->9", "a->2+a->8", "a->2+a->9", "a->3+a->8", "a->3+a->9",
                "b->1+b->8", "b->1+b->9", "b->2+b->8", "b->2+b->9", "b->3+b->8", "b->3+b->9");
        moveSelector.stepEnded(stepScopeA1);

        AbstractStepScope stepScopeA2 = mock(AbstractStepScope.class);
        when(stepScopeA2.getPhaseScope()).thenReturn(phaseScopeA);
        moveSelector.stepStarted(stepScopeA2);
        assertAllCodesOfMoveSelector(moveSelector, DO_NOT_ASSERT_SIZE,
                "a->1+a->8", "a->1+a->9", "a->2+a->8", "a->2+a->9", "a->3+a->8", "a->3+a->9",
                "b->1+b->8", "b->1+b->9", "b->2+b->8", "b->2+b->9", "b->3+b->8", "b->3+b->9");
        moveSelector.stepEnded(stepScopeA2);

        moveSelector.phaseEnded(phaseScopeA);

        moveSelector.solvingEnded(solverScope);

        verifySolverPhaseLifecycle(entitySelector, 1, 1, 2);
        verifySolverPhaseLifecycle(primaryValueSelector, 1, 1, 2);
        verifySolverPhaseLifecycle(secondaryValueSelector, 1, 1, 2);
    }

    @Test
    public void randomMimicNotIgnoringEmpty() {
        randomMimic(false);
    }

    @Test
    public void randomMimicIgnoringEmpty() {
        randomMimic(true);
    }

    public void randomMimic(boolean ignoreEmptyChildIterators) {
        EntitySelector entitySelector = SelectorTestUtils.mockEntitySelector(TestdataMultiVarEntity.class,
                new TestdataMultiVarEntity("a"), new TestdataMultiVarEntity("b"));
        MimicRecordingEntitySelector recordingEntitySelector = new MimicRecordingEntitySelector(
                entitySelector);
        ValueSelector primaryValueSelector = SelectorTestUtils.mockValueSelector(
                TestdataMultiVarEntity.class, "primaryValue",
                new TestdataValue("1"), new TestdataValue("2"), new TestdataValue("3"));
        ValueSelector secondaryValueSelector = SelectorTestUtils.mockValueSelector(
                TestdataMultiVarEntity.class, "secondaryValue",
                new TestdataValue("8"), new TestdataValue("9"));

        List<MoveSelector> moveSelectorList = new ArrayList<MoveSelector>(2);
        moveSelectorList.add(new ChangeMoveSelector(
                recordingEntitySelector,
                primaryValueSelector,
                false));
        moveSelectorList.add(new ChangeMoveSelector(
                new MimicReplayingEntitySelector(recordingEntitySelector),
                secondaryValueSelector,
                false));
        MoveSelector moveSelector = new CartesianProductMoveSelector(moveSelectorList,
                ignoreEmptyChildIterators, true);

        DefaultSolverScope solverScope = mock(DefaultSolverScope.class);
        moveSelector.solvingStarted(solverScope);

        AbstractSolverPhaseScope phaseScopeA = mock(AbstractSolverPhaseScope.class);
        when(phaseScopeA.getSolverScope()).thenReturn(solverScope);
        moveSelector.phaseStarted(phaseScopeA);

        AbstractStepScope stepScopeA1 = mock(AbstractStepScope.class);
        when(stepScopeA1.getPhaseScope()).thenReturn(phaseScopeA);
        moveSelector.stepStarted(stepScopeA1);
        assertCodesOfNeverEndingMoveSelector(moveSelector, 24L,
                "a->1+a->8", "a->2+a->9", "a->3+a->8", "b->1+a->9", "b->2+b->8", "b->3+b->9");
        moveSelector.stepEnded(stepScopeA1);

        moveSelector.phaseEnded(phaseScopeA);

        moveSelector.solvingEnded(solverScope);

        verifySolverPhaseLifecycle(entitySelector, 1, 1, 1);
        verifySolverPhaseLifecycle(primaryValueSelector, 1, 1, 1);
        verifySolverPhaseLifecycle(secondaryValueSelector, 1, 1, 1);
    }

}
