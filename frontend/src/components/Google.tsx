const Google = () => {
  const googleClientId = '988799632073-ghj80ioi4mo9jnb9dchjbjtaucmrmm81.apps.googleusercontent.com';
  const googleRedirectUrl = 'http://localhost:3000/googleLogin';
  const googleAuthUrl = `https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=${googleClientId}&scope=openid%20profile%20email&redirect_uri=${googleRedirectUrl}`;

  const loginHandler = () => {
    window.location.href = googleAuthUrl;
  };
  return (
    <div>
      <>
      <button onClick={loginHandler}>구글 로그인</button>
      </>
    </div>
  )
}

export default Google;