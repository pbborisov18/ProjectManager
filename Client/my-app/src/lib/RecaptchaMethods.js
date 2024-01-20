export const handleCaptchaError = () => {
    console.log("well...Google (or me) fed up with their recaptcha stuff. I'm too lazy to put this in a toast or something since it isn't in a svelte file. ");
};

//No idea wtf it does...
//Docs say it resets the captcha
//Probably when it's failed ?
export const resetCaptcha = () => {
    window.grecaptcha.reset();
};