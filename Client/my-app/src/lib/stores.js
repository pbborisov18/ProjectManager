import {writable} from "svelte/store";
import {browser} from "$app/environment";

export const userEmail = writable(browser && localStorage.getItem('userEmail'));
export const loggedIn = writable(browser && localStorage.getItem('loggedIn'));
export const company = writable(browser && sessionStorage.getItem('company'));
export const project = writable(browser && sessionStorage.getItem('project'));
export const team = writable(browser && sessionStorage.getItem('team'));

userEmail.subscribe(value => {
    browser &&
    (localStorage.userEmail = value);
});

loggedIn.subscribe(value => {
    browser &&
    (localStorage.loggedIn = value);
});

company.subscribe(value => {
    browser &&
    (sessionStorage.company = value);
});

project.subscribe(value => {
    browser &&
    (sessionStorage.project = value);
});

team.subscribe(value => {
    browser &&
    (sessionStorage.team = value);
});