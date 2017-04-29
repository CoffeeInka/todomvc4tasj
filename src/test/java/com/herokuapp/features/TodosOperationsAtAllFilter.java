package com.herokuapp.features;

import com.herokuapp.base.BaseTest;
import com.herokuapp.categories.Buggy;
import com.herokuapp.pages.ToDoMVCPage;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static com.herokuapp.pages.ToDoMVCPage.TaskStatus.ACTIVE;
import static com.herokuapp.pages.ToDoMVCPage.TaskStatus.COMPLETED;

/**
 * Created by inna on 21/04/2017.
 */


public class TodosOperationsAtAllFilter extends BaseTest {

    ToDoMVCPage page = new ToDoMVCPage();

    @Test
    public void completeAll() {
        page.given(page.aTask(ACTIVE, "1"), page.aTask(COMPLETED, "2"));

        page.toggleAll();
        page.assertTasks("1", "2");
        page.assertItemsLeft(0);
    }

    @Test
    public void clearCompleted() {
        page.given(COMPLETED, "1");

        page.clearCompleted();
        page.assertNoTasks();
    }

    @Test
    public void reactivate() {
        page.given(page.aTask(ACTIVE, "1"), page.aTask(COMPLETED, "2"));

        page.toggle("2");
        page.assertTasks("1", "2");
        page.assertItemsLeft(2);
    }

    @Test
    public void edit() {
        page.given(ACTIVE, "1");

        page.edit("1", "1 edited");
        page.assertTasks("1 edited");
        page.assertItemsLeft(1);
    }

    @Test
    public void delete() {
        page.given(page.aTask(COMPLETED, "1"), page.aTask(ACTIVE, "2"));

        page.delete("1");
        page.assertTasks("2");
        page.assertItemsLeft(1);
    }

    //buggy test
    @Test
    @Category(Buggy.class)
    public void cancelEdit() {
        page.given(page.aTask(ACTIVE, "1"), page.aTask(COMPLETED, "2"));

        page.cancelEdit("2", "2 edit canceled");
        page.assertTasks("1", "2");
        //page.assertItemsLeft(1);
        page.assertItemsLeft(2);
    }

    @Test
    public void editByTab() {
        page.given(COMPLETED, "1");

        page.editByTab("1", "1 edited");
        page.assertTasks("1 edited");
        page.assertItemsLeft(0);
    }

}
