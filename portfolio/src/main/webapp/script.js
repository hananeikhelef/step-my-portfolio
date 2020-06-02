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

/**
 * Adds a random greeting to th.
 */
 
function toggleClass(){
	var element = document.getElementById('contact');
	element.classList.toggle("active")
}
/**
 * Fetches a message from the server and adds it to the DOM.
 */
function getMessage() {
  fetch('/data').then(response => response.text()).then((message) => {
    document.getElementById('message-container').innerText = message;
  });
}

/**
 * Fetches stats from the servers and adds them to the DOM.
 */
async function getData() {
  fetch('/data').then(response => response.json()).then((stats) => {
    // stats is an object, not a string, so we have to
    // reference its fields to create HTML content

    const statsListElement = document.getElementById('message-container');
    statsListElement.innerHTML = '';

    for(const x in stats){
        statsListElement.appendChild(
        createListElement(stats[x]));
    }
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
