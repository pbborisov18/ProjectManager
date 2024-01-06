export function passwordMatch(pass, confirmPass) {
    return pass === confirmPass;
}

export function containNumbers(pass, numbers) {
    return pass.replace(/[^0-9]/g,"").length >= numbers;
}