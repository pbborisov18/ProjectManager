import {writable} from "svelte/store";

let initialEmailValue = '';
//this doesn't work for some weird reason lol
//null as default. stupid js
let initialLogin = "false";
let initialCompany = "";
let initialProject = "";
let initialTeam = "";

if (typeof window !== 'undefined') {
    initialEmailValue = localStorage.getItem('userEmail');
    initialLogin = localStorage.getItem('loggedIn');
    initialCompany = sessionStorage.getItem('company');
    initialProject = sessionStorage.getItem('project');
    initialTeam = sessionStorage.getItem('team');
}

export const userEmail = writable(initialEmailValue);
export const loggedIn = writable(initialLogin);
export const company = writable(initialCompany);
export const project = writable(initialProject);
export const team = writable(initialTeam);


if (typeof window !== 'undefined') {
    userEmail.subscribe(value => {
        localStorage.setItem('userEmail', value);
    });

    loggedIn.subscribe(value => {
        localStorage.setItem('loggedIn', value);
    });

    company.subscribe(value => {
        sessionStorage.setItem('company', value);
    });

    project.subscribe(value => {
        sessionStorage.setItem('project', value);
    });

    team.subscribe(value => {
        sessionStorage.setItem('team', value);
    });

}