import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import PasswordInput from "./inputs/PasswordInput";
import TextInput from "./inputs/TextInput";

const LogInPage = () => {
    return ( 
        <main className="loginPage panel-wrapper">
            <LogInSection />
            <RegisterOptionSection />
        </main>
     );
}


const LogInSection = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [wrongData, setWrongData] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();


                //Send data to backend and get true or false answer
        // const customerLogInData = { email, password };
        // fetch('http://localhost:8000/login', {
        //     method: "POST",
        //     headers: { "Content-Type": "application/json" },
        //     body: JSON.stringify(customerLogInData)
        // })
        // .then( resp => { console.log(resp) });

                //Temporarily I use GET METHOD
        fetch('http://localhost:8000/users/' + email)
        .then(resp => resp.json())
        .then(data => {
            console.log(data)
            if (Object.keys(data).length === 0 || data.password !== password ) {
                setWrongData(true);

            } else {
                sessionStorage.setItem("id", data.id)
                sessionStorage.setItem("name", data.firstName)

                navigate('/')
            }
        })
    }

    return (
        <section className="loginPage__login-section panel-wrapper__panel--no-image-background">
            <h2 className="panel-wrapper__title">Log In to Your Account</h2>
            <form className="form" onSubmit={handleSubmit}>
                <div className="form__fields">
                    <TextInput state={email} setState={setEmail} isRequired={true} placeholder="Email"/>
                    <PasswordInput state={password} setState={setPassword} isRequired={true} placeholder="Password" />
                    {wrongData && <p className="incorrect-data-text">Incorrect email or password</p>}
                </div>
                <button className="btn btn--login">Log In</button>
            </form>
        </section>
    )
}

const RegisterOptionSection = () => {
    return (
        <section className="loginPage__register-text-section panel-wrapper__panel--image-background">
            <h2 className="panel-wrapper__title">New to Bookstore?</h2>
            <p className="panel-wrapper__paragraph">Sing up and discover a great amount of new opportunities!</p>
            <Link to='/register' className="panel-wrapper__link">SIGN UP</Link>
        </section>
    )
}
 
export default LogInPage;