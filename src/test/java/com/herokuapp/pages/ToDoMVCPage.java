package com.herokuapp.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.apache.commons.lang3.StringUtils;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Arrays;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;

/**
 * Created by inna on 4/5/17.
 */
public class ToDoMVCPage {

    public ElementsCollection tasks = $$("#todo-list li");

    public ElementsCollection filters = $$("#filters li");


    @Step
    public void add(String... tasksTexts) {
        for (String text : tasksTexts) {
            $("#new-todo").shouldBe(enabled).setValue(text).pressEnter();
        }
    }

    @Step
    public void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
    }

    @Step
    public void toggle(String taskText) {
        tasks.findBy(exactText(taskText)).find(".toggle").click();
    }

    @Step
    public void toggleAll() {
        $("#toggle-all").click();
    }

    @Step
    public void assertItemsLeft(int count) {
        $("#todo-count strong").shouldHave(text(Integer.toString(count)));
    }

    @Step
    public void clearCompleted() {
        $("#clear-completed").click();
    }

    @Step
    public SelenideElement startEdit(String oldTaskText, String newTaskText) {
        tasks.findBy(exactText(oldTaskText)).doubleClick();
        return tasks.findBy(cssClass("editing")).find(".edit").setValue(newTaskText);
    }

    @Step
    public void edit(String oldTaskText, String newTaskText) {
        startEdit(oldTaskText, newTaskText).pressEnter();
    }

    @Step
    public void editByTab(String oldTaskText, String newTaskText) {
        startEdit(oldTaskText, newTaskText).pressTab();
    }

    @Step
    public void editByClickOutOfTask(String oldTaskText, String newTaskText) {
        startEdit(oldTaskText, newTaskText);
        $("#header h1").click();
    }

    @Step
    public void cancelEdit(String oldTaskText, String newTaskText) {
        startEdit(oldTaskText, newTaskText).pressEscape();
    }

    @Step
    public void filterCompleted() {
        filters.findBy(exactText("Completed")).click();
    }

    @Step
    public void filterActive() {
        filters.findBy(exactText("Active")).click();
    }

    @Step
    public void filterAll() {
        filters.findBy(exactText("All")).click();
    }

    @Step
    public void assertNoTasks() {
        tasks.filterBy(visible).shouldBe(empty);
    }

    @Step
    public void assertTasks(String... tasksTexts) {
        tasks.filterBy(visible).shouldHave(exactTexts(tasksTexts));
    }

    public void ensureUrl() {
        if (!url().equals("https://todomvc4tasj.herokuapp.com/")) {
            open("https://todomvc4tasj.herokuapp.com/");
        }
    }

    public class Task {

        TaskStatus status;
        String taskText;

        Task(TaskStatus status, String taskText) {
            this.status = status;
            this.taskText = taskText;
        }

        @Override
        public String toString() {
            return String.format("{\"completed\":%s,\"title\":\"%s\"}", status, taskText);
        }
    }

    @Step
    public Task aTask(TaskStatus status, String taskText) {
        Task aTask = new Task(status, taskText);
        return aTask;
    }

    public enum TaskStatus {
        ACTIVE("false"),
        COMPLETED("true");

        public String status;

        TaskStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }

    @Step
    public Task[] tasksWithStatus(TaskStatus status, String... taskTexts) {
//        ArrayList<Task> tasks = new ArrayList<Task>();
//        for (String taskText : taskTexts) {
//            tasks.add(aTask(status, taskText));}
//        return tasks.toArray(new Task[tasks.size()]);

        return Arrays.stream(taskTexts).map(taskText ->
                aTask(status, taskText)).
                toArray(size -> new Task[size]);
    }

    @Step
    public void given(Task... tasks) {
        ensureUrl();
        String jsCommand = "localStorage.setItem(\"todos-troopjs\", '[" + StringUtils.join(tasks, ",") + "]')";
        System.out.println(jsCommand);
        executeJavaScript(jsCommand);
        refresh();
    }

    @Step
    public void given(TaskStatus status, String... taskTexts) {
        given(tasksWithStatus(status, taskTexts));
    }

    @Step
    public void givenAtActive(Task... tasks) {
        given(tasks);
        filterActive();
    }

    @Step
    public void givenAtActive(TaskStatus status, String... taskTexts) {
        given(tasksWithStatus(status, taskTexts));
        filterActive();
    }

    @Step
    public void givenAtCompleted(Task... tasks) {
        given(tasks);
        filterCompleted();
    }

    @Step
    public void givenAtCompleted(TaskStatus status, String... taskTexts) {
        given(tasksWithStatus(status, taskTexts));
        filterCompleted();
    }
}
