package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotePage {
    public NotePage(WebDriver driver) {
        PageFactory.initElements(driver,this);
    }

    // notes Elements
    @FindBy(id="add-new-note")
    private WebElement addNoteBtn;

    @FindBy(className="edit-note-open")
    private WebElement editNoteModel;


    // add modal
    @FindBy(id="noteModel")
    private WebElement noteModel;


    @FindBy(id="noteTitle")
    private WebElement noteTitle;

    @FindBy(id="noteDescription")
    private WebElement noteDescription;

    @FindBy(id="noteAddSave")
    private WebElement noteAddSave;

    @FindBy(id="noteAddCancel")
    private WebElement noteAddCancel;

    /* ######### edit modal ########### */
    @FindBy(id="noteEditModel")
    private WebElement noteEditModel;

    @FindBy(id="noteEditId")
    private WebElement noteEditId;

    @FindBy(id="noteEditTitle")
    private WebElement noteEditTitle;

    @FindBy(id="noteEditDescription")
    private WebElement noteEditDescription;

    @FindBy(id="noteEditSave")
    private WebElement noteEditSave;

    @FindBy(id="noteEditCancel")
    private WebElement noteEditCancel;

    @FindBy(className="note-title-text")
    private WebElement notesTitles;
    // new way

    @FindBy(how = How.CLASS_NAME, using="note-remove")
    private List<WebElement> deleteNoteBtn;


    @FindBy(how = How.CLASS_NAME, using="note-desc-text")
    private List<WebElement> notesDescriptions;

    public WebElement getNotes(){
        return notesTitles;
    }
    public String createRandomNote() throws InterruptedException {
        addNoteBtn.click();
        Thread.sleep(300);
        String title = "Topic Number " + (int)(Math.random() * 10) + "_" + (int)(Math.random() * 10);
        String desc = "Current Desc About Number" + (int)(Math.random() * 10) + "_" + (float)(Math.random() * 55455444.121331313311233131133131);

        noteTitle.clear();
        noteTitle.sendKeys(title);
        Thread.sleep(300);
        noteDescription.clear();
        noteDescription.sendKeys(desc);

        // save
        noteAddSave.click();

        Thread.sleep(500);
        return title;

    }

    public boolean isElementWithThisIdExist(WebDriver driver, String id){
        return driver.findElement(By.id(id)) != null;
    }

    public WebElement getElementById(WebDriver driver, String id){
        try{
            return driver.findElement(By.id(id));
        } catch (NoSuchElementException e){
            return null;
        }

    }

    public void deleteNoteById(WebDriver driver, String id){
        WebElement deleteNoteBtn = driver.findElement(By.id(id));
        if (deleteNoteBtn != null){
            deleteNoteBtn.submit();
        }
    }

    public void openEditNoteModelById(WebDriver driver, String id){
        WebElement openEditNoteBtn = driver.findElement(By.id(id));
        if (openEditNoteBtn != null){
            openEditNoteBtn.click();
        }
    }

    public void fillNoteEditTitle(String newTitle){
        noteEditTitle.clear();
        noteEditTitle.sendKeys(newTitle);
    }
    public void fillNoteEditDesc(String newDescription){
        noteEditDescription.clear();
        noteEditDescription.sendKeys(newDescription);
    }

    public void submitEditNote(){
        noteEditSave.click();
    }

    public List<String> getAllNotesText(WebDriver driver){
        List<String> notesText = new ArrayList<>();
        List<WebElement> allNotes = driver.findElements(By.cssSelector(".note-title-text"));
        for (WebElement allNote : allNotes) {
            notesText.add(allNote.getText());
        }
        return notesText;
    }

    public List<String> getAllDescriptionText(WebDriver driver){
        List<String> descTexts = new ArrayList<>();
        List<WebElement> allDescriptions = driver.findElements(By.className("note-desc-text"));
        System.out.println(allDescriptions.size());
        for (WebElement allDescription : allDescriptions) {
            descTexts.add(allDescription.getText());
        }
        return descTexts;

    }

    public List<String> getNotePartList(WebDriver driver, String part) {
        if (Objects.equals(part, "title")){
            return getAllNotesText(driver);
        } else if (Objects.equals(part, "desc")){
            return getAllDescriptionText(driver);
        } else {
            System.out.println(".1.");
            return getAllDescriptionText(driver);
        }
    }

        public void openEditNoteModel() {
        editNoteModel.click();
    }
}
