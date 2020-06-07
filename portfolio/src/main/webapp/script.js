// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


 /** Toggle to show comment section in the contact page */
 
function toggleClass(){
	var element = document.getElementById('contact');
	element.classList.toggle("active")
}
/**
 * Fetches a message from the server and adds it to the DOM.
 */
// function getMessage() {
//   fetch('/data').then(response => response.text()).then((message) => {
//     document.getElementById('message-container').innerText = message;
//   });
// }

/**
 * Fetches stats from the servers and adds them to the DOM.
 */
async function getData() {
  fetch('/data').then(response => response.json()).then((tasks) => {
    const taskListElement = document.getElementById('message-container');    
    taskListElement.innerHTML="";
    
    for(var i = 0; i < tasks.length; i++){
        taskListElement.appendChild(createListElement(tasks));
    }
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.textContent =  text.content;
    return liElement;
//   const taskElement = document.createElement('li');
//   taskElement.className = 'task';

//   const titleElement = document.createElement('span');
//   titleElement.innerText = task.title;

//   const deleteButtonElement = document.createElement('button');
//   deleteButtonElement.innerText = 'Delete';
//   deleteButtonElement.addEventListener('click', () => {
//     deleteTask(task);

//     // Remove the task from the DOM.
//     taskElement.remove();
//   });

//   taskElement.appendChild(titleElement);
//   taskElement.appendChild(deleteButtonElement);
//   return taskElement;
}

/** Tells the server to delete the task. */
function deleteTask(message) {
  const params = new URLSearchParams();
  params.append('id', message);
  fetch('/delete-data', {method: 'POST', body: params});
}