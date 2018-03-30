/**
 * @author Samson Leung 110490519
 */
package csg.courseDetails;

import csg.CsgProp;
import csg.CSGWorkspace;
import csg.taTab.TeachingAssistant;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import djf.ui.AppGUI;
import java.io.File;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import properties_manager.PropertiesManager;

public class CourseWorkspace extends AppWorkspaceComponent{

    VBox sections, siteTemp;
    GridPane courseGrid, pageStyleGrid;
    Label info, site, page,
            subject, number, semester, year, title, name, home, export, exportPath,
            sitePages,
            bannerImgLabel, leftImgLabel, rightImgLabel, stylesheetLabel, noteLabel;

    ComboBox subjectCombo, semesterCombo, numCombo, yearCombo, cssCombo;

    TextField titleText, nameText, homeText;
    Text selectedDir, tempDir;

    Button changeButton, selectTempDir, bannerChangeBtn, leftChangeBtn, rightChangeBtn;

    TableView<SitePage> sitePagesTable;

    ImageView bannerImg, leftFooterImg, rightFooterImg;
    String bannerPicture, leftPicture, rightPicture;

    ObservableList<String> subjectOptions, semesterOptions, numberOptions, yearOptions, cssOptions;

    CSGWorkspace space;
    CourseDetailsData data;
    PropertiesManager props;
    
    String navBar;
    
    public CourseWorkspace(CSGWorkspace space) {

        this.data = space.getCourseSiteGenerator().getDataComponent().getCourseDetailsData();
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        props = PropertiesManager.getPropertiesManager();
        this.space = space;

        sections = new VBox();
        sections.setStyle("-fx-spacing: 10; -fx-padding: 7;");

        //COURSE INFO SECTION
        courseGrid = new GridPane();
        courseGrid.setHgap(10);
        courseGrid.setVgap(10);
        courseGrid.setStyle("-fx-padding: 7;");

        info = new Label(props.getProperty(CsgProp.COURSE_INFO.toString()));
        courseGrid.add(info, 0, 0);

        subject = new Label(props.getProperty(CsgProp.SUBJECT.toString()));
        courseGrid.add(subject, 0, 1);

        number = new Label(props.getProperty(CsgProp.NUMBER.toString()));
        courseGrid.add(number, 2, 1);

        semester = new Label(props.getProperty(CsgProp.SEMESTER.toString()));
        courseGrid.add(semester, 0, 2);

        year = new Label(props.getProperty(CsgProp.YEAR.toString()));
        courseGrid.add(year, 2, 2);

        title = new Label(props.getProperty(CsgProp.TITLE.toString()));
        courseGrid.add(title, 0, 3);

        name = new Label(props.getProperty(CsgProp.NAME.toString()));
        courseGrid.add(name, 0, 4);

        home = new Label(props.getProperty(CsgProp.HOME.toString()));
        courseGrid.add(home, 0, 5);

        export = new Label(props.getProperty(CsgProp.EXPORT.toString()));
        courseGrid.add(export, 0, 6);

        exportPath = new Label(props.getProperty(CsgProp.NONE_SELECTED.toString()));
        courseGrid.add(exportPath, 1, 6);

        changeButton = new Button(props.getProperty(CsgProp.CHANGE_BUTTON.toString()));
        courseGrid.add(changeButton, 2, 6);
        changeButton.setOnAction(e -> {
            DirectoryChooser choose = new DirectoryChooser();
            choose.setTitle(props.getProperty(CsgProp.SELECT_EXPORT.toString()));
            File selectedDirectory = choose.showDialog(space.getCourseSiteGenerator().getGUI().getWindow());
            
            if (selectedDirectory == null) {
                exportPath.setText(props.getProperty(CsgProp.NONE_SELECTED.toString()));
                space.getCourseSiteGenerator().getGUI().getExportButton().setDisable(true);
            } else {
                String path = selectedDirectory.getAbsolutePath();
                exportPath.setText(path);
                space.getCourseSiteGenerator().getGUI().getExportButton().setDisable(false);
                space.getCourseSiteGenerator().getGUI().getFileController().setExportPath(path);
                data.saveData();
                markWorkAsEdited();
            }
        });

        subjectOptions = FXCollections.observableArrayList("AMS", "BUS", "CSE", "SPN");
        subjectCombo = new ComboBox(subjectOptions);
        courseGrid.add(subjectCombo, 1, 1);

        numberOptions = FXCollections.observableArrayList("114", "119", "219", "220");
        numCombo = new ComboBox(numberOptions);
        courseGrid.add(numCombo, 3, 1);

        yearOptions = FXCollections.observableArrayList("2017", "2018", "2019", "2020");
        yearCombo = new ComboBox(yearOptions);
        courseGrid.add(yearCombo, 3, 2);

        semesterOptions = FXCollections.observableArrayList("Fall", "Winter", "Spring", "Summer");
        semesterCombo = new ComboBox(semesterOptions);
        courseGrid.add(semesterCombo, 1, 2);

        titleText = new TextField();
        courseGrid.add(titleText, 1, 3);

        nameText = new TextField();
        courseGrid.add(nameText, 1, 4);

        homeText = new TextField();
        courseGrid.add(homeText, 1, 5);

        //SITE TEMPLATE SECTION
        siteTemp = new VBox();
        siteTemp.setStyle("-fx-padding: 7; -fx-spacing: 10");

        site = new Label(props.getProperty(CsgProp.SITE_TEMPLATE.toString()));

        selectedDir = new Text(props.getProperty(CsgProp.SELECTED_DIRECTORY.toString()));

        tempDir = new Text(props.getProperty(CsgProp.NONE_SELECTED.toString()));
        tempDir.setStyle("-fx-font-weight: bold;");
        
        selectTempDir = new Button(props.getProperty(CsgProp.SELECT_TEMPLATE.toString()));
        selectTempDir.setOnAction(e -> {
            DirectoryChooser choose = new DirectoryChooser();
            choose.setTitle(props.getProperty(CsgProp.SELECT_TEMPLATE.toString()));
            File selectedDirectory = choose.showDialog(space.getCourseSiteGenerator().getGUI().getWindow());

            if(selectedDirectory != null)
                data.getPages().clear();
                loadSites(selectedDirectory);
                markWorkAsEdited();
                data.saveData();
        });

        sitePages = new Label(props.getProperty(CsgProp.SITE_PAGES.toString()));
        
        sitePagesTable = new TableView();
        sitePagesTable.setItems(data.getPages());
        sitePagesTable.setEditable(true);
        //sitePagesTable.prefHeightProperty().bind(siteTemp.heightProperty().multiply(0.5));

        TableColumn useCol = new TableColumn(props.getProperty(CsgProp.USE.toString()));
        TableColumn navbarCol = new TableColumn(props.getProperty(CsgProp.NAVBAR.toString()));
        TableColumn fileCol = new TableColumn(props.getProperty(CsgProp.FILE_NAME.toString()));
        TableColumn scriptCol = new TableColumn(props.getProperty(CsgProp.SCRIPT.toString()));
        
        useCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SitePage, CheckBox>, ObservableValue<CheckBox>>(){
            @Override
            public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<SitePage, CheckBox> c) {
                SitePage site = c.getValue();
                CheckBox checkBox = new CheckBox();
                checkBox.setOnAction(e -> {
                    site.setUse(checkBox.selectedProperty().get());
                    markWorkAsEdited();
                });

                checkBox.selectedProperty().setValue(site.isUse());
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> a, Boolean oldVal, Boolean newVal) {
                        site.setUse(newVal);
                    }
                });
                return new SimpleObjectProperty<CheckBox>(checkBox);
            }
        });
        navbarCol.setCellValueFactory(new PropertyValueFactory<>("navBar"));
        fileCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        scriptCol.setCellValueFactory(new PropertyValueFactory<>("script"));
        
        sitePagesTable.getColumns().addAll(useCol, navbarCol, fileCol, scriptCol);

        siteTemp.getChildren().addAll(site, selectedDir, tempDir, selectTempDir, sitePages, sitePagesTable);

        //PAGE STYLE SECTION
        pageStyleGrid = new GridPane();
        pageStyleGrid.setHgap(10);
        pageStyleGrid.setVgap(10);
        pageStyleGrid.setStyle("-fx-padding: 7;");

        page = new Label(props.getProperty(CsgProp.PAGE_STYLE.toString()));
        pageStyleGrid.add(page, 0, 0);

        bannerImgLabel = new Label(props.getProperty(CsgProp.BANNER_IMAGE_LABEL.toString()));
        pageStyleGrid.add(bannerImgLabel, 0, 1);
        
        bannerImg = new ImageView();
        pageStyleGrid.add(bannerImg, 1, 1);

        leftImgLabel = new Label(props.getProperty(CsgProp.LEFT_IMAGE_LABEL.toString()));
        pageStyleGrid.add(leftImgLabel, 0, 2);

        leftFooterImg = new ImageView();
        pageStyleGrid.add(leftFooterImg, 1, 2);

        rightImgLabel = new Label(props.getProperty(CsgProp.RIGHT_IMAGE_LABEL.toString()));
        pageStyleGrid.add(rightImgLabel, 0, 3);

        rightFooterImg = new ImageView();
        pageStyleGrid.add(rightFooterImg, 1, 3);

        stylesheetLabel = new Label(props.getProperty(CsgProp.STYLESHEET_LABEL.toString()));
        pageStyleGrid.add(stylesheetLabel, 0, 4);

        File folder = new File("./work/css");
        cssOptions = FXCollections.observableArrayList();
        
        for(File f: folder.listFiles()){
            cssOptions.add(f.getName());
        }
        
        cssCombo = new ComboBox(cssOptions);
        cssCombo.setOnAction(e -> {
            String file = (String) cssCombo.getValue();
            for(File f: folder.listFiles()){
                if(f.getName().equals(file)){
                    data.setStyleSheet(f.getAbsolutePath());
                    break;
                }
            }
            markWorkAsEdited();
        });

        pageStyleGrid.add(cssCombo, 1, 4);

        noteLabel = new Label(props.getProperty(CsgProp.NOTE_LABEL.toString()));
        pageStyleGrid.add(noteLabel, 1, 5);
        
        bannerPicture = leftPicture = rightPicture = null;

        bannerChangeBtn = new Button(props.getProperty(CsgProp.CHANGE_BUTTON.toString()));
        pageStyleGrid.add(bannerChangeBtn, 2, 1);
        bannerChangeBtn.setOnAction(e -> {
            FileChooser choose = new FileChooser();
            bannerPicture = "file:" + choose.showOpenDialog(space.getCourseSiteGenerator().getGUI().getWindow()).getAbsolutePath();
            if (bannerPicture != null) {
                bannerImg.setImage(new Image(bannerPicture, 100, 100, true, true));
                markWorkAsEdited();
            }
        });

        leftChangeBtn = new Button(props.getProperty(CsgProp.CHANGE_BUTTON.toString()));
        pageStyleGrid.add(leftChangeBtn, 2, 2);
        leftChangeBtn.setOnAction(e -> {
            FileChooser choose = new FileChooser();
            leftPicture = "file:" + choose.showOpenDialog(space.getCourseSiteGenerator().getGUI().getWindow()).getAbsolutePath();
            if (leftPicture != null) {
                leftFooterImg.setImage(new Image(leftPicture, 100, 100, true, true));
                markWorkAsEdited();
            }
        });

        rightChangeBtn = new Button(props.getProperty(CsgProp.CHANGE_BUTTON.toString()));
        pageStyleGrid.add(rightChangeBtn, 2, 3);
        rightChangeBtn.setOnAction(e -> {
            FileChooser choose = new FileChooser();
            rightPicture = "file:" + choose.showOpenDialog(space.getCourseSiteGenerator().getGUI().getWindow()).getAbsolutePath();
            if (rightPicture != null) {
                rightFooterImg.setImage(new Image(rightPicture, 100, 100, true, true));
                markWorkAsEdited();
            }
        });

        sections.getChildren().addAll(courseGrid, siteTemp, pageStyleGrid);
        space.getCourseTab().setContent(sections);
    }

    public GridPane getCourseInfo() {
        return courseGrid;
    }

    public Pane getSiteTemplate() {
        return siteTemp;
    }

    public Pane getPageStyle() {
        return pageStyleGrid;
    }

    public Label getInfo() {
        return info;
    }

    public Label getSite() {
        return site;
    }

    public Label getPage() {
        return page;
    }
    
    public void loadSites(File dir) {
        try {
            tempDir.setText(dir.getAbsolutePath());
            for (File file : dir.listFiles()) {
                if (file.getName().equals("index.html")) {
                    data.getPages().add(new SitePage(true, "Home", "index.html", "HomeBuilder.js", file));
                } else if (file.getName().equals("syllabus.html")) {
                    data.getPages().add(new SitePage(true, "Syllabus", "syllabus.html", "SyllabusBuilder.js", file));
                } else if (file.getName().equals("schedule.html")) {
                    data.getPages().add(new SitePage(true, "Schedule", "schedule.html", "ScheduleBuilder.js", file));
                } else if (file.getName().equals("hws.html")) {
                    data.getPages().add(new SitePage(true, "HWs", "hws.html", "HWsBuilder.js", file));
                } else if (file.getName().equals("projects.html")) {
                    data.getPages().add(new SitePage(true, "Projects", "projects.html", "ProjectsBuilder.js", file));
                }
            }
        } catch (NullPointerException e) {
            tempDir.setText(props.getProperty(CsgProp.NONE_SELECTED.toString()));
        }

    }
    
    private void markWorkAsEdited() {
        // MARK WORK AS EDITED
        AppGUI gui = space.getCourseSiteGenerator().getGUI();
        gui.getFileController().markAsEdited(gui);
    }

    @Override
    public void resetWorkspace() {
        subjectCombo.getSelectionModel().clearSelection();
        numCombo.getSelectionModel().clearSelection();
        semesterCombo.getSelectionModel().clearSelection();
        yearCombo.getSelectionModel().clearSelection();
        titleText.clear();
        nameText.clear();
        homeText.clear();
        exportPath.setText("None Selected");
        tempDir.setText("None Selected");
        data.getPages().clear();
        bannerImg.setImage(null);
        leftFooterImg.setImage(null);
        rightFooterImg.setImage(null);
        cssCombo.getSelectionModel().clearSelection();
    }

    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
