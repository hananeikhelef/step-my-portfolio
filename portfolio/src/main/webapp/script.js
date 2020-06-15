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
 * Fetches stats from the servers and adds them to the DOM.
 */
async function getData() {
  fetch('/data').then(response => response.json()).then((messages) => {
    const messageListElement = document.getElementById('message-container');    
    messageListElement.innerHTML="";
    
    for(const x in messages){
        if(messages[x].content !== undefined){
            messageListElement.appendChild(createListElement(messages[x]));
        }
    }
  });
}

/** Creates an <li> element containing the message content . */
function createListElement(text) {
  const messageElement = document.createElement('li');
  messageElement.textContent =  text.content;

  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = 'Delete';
  deleteButtonElement.addEventListener('click', () => {
    deleteMessage(text);

   // Remove the message from the DOM
    messageElement.remove();
  });

  messageElement.appendChild(deleteButtonElement);
  return messageElement;
}

/** Tells the server to delete the comment. */
function deleteMessage(message) {
  const params = new URLSearchParams();
  params.append('id', message);
  fetch('/delete-data', {method: 'POST', body: params});
}

async function comment() {
    const respone = await fetch('/authentication');
    const url = await respone.json(); 

    const sign_in = document.querySelector("#button");
    
    if(respone.status === 200){ 
        sign_in.href = url[0];
    }
    else if(respone.status === 403){ 
        sign_in.href = url[0];                               
    }
}

window.onload = function(){
    getData();
    comment();
}
