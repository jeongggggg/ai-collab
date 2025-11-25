export default function Login() {
  const CLIENT_ID = "Ov23liXibuU5rGDfRWZg";
  const REDIRECT_URI = "http://localhost:5173/auth/callback";

  const loginWithGithub = () => {
    const url =
      `https://github.com/login/oauth/authorize` +
      `?client_id=${CLIENT_ID}` +
      `&redirect_uri=${REDIRECT_URI}` +
      `&scope=read:user user:email`;
    window.location.href = url;
  };

  return (
    <div>
      <h1>Login</h1>
      <button onClick={loginWithGithub}>Login with GitHub</button>
    </div>
  );
}