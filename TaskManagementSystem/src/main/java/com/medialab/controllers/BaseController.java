package com.medialab.controllers;

import com.medialab.models.TaskManager;

public class BaseController {
        protected MainController mainController;
        protected TaskManager taskManager;
    
        public void setMainController(MainController mainController) {
            this.mainController = mainController;
        }
    
        public MainController getMainController() {
            return mainController;
        }
    
        public void setTaskManager(TaskManager taskManager) {
            this.taskManager = taskManager;
    }

}
