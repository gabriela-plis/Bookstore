import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import PasswordInput from "../../reusable-components/PasswordInput";
import TextInput from "../../reusable-components/TextInput";
import LoginData from "../../DTO/LoginDataDTO";


type Props = {
    setIsAuthenticated: React.Dispatch<React.SetStateAction<boolean>>;
}

const LogInPage = (props: Props) => {
    return ( 
        <main className="loginPage panel-wrapper">
            <LogInSection setIsAuthenticated={props.setIsAuthenticated}/>
            <RegisterOptionSection />
        </main>
     );
}


const LogInSection = (props: Props) => {
    const [loginData, setLoginData] = useState<LoginData>({
        email: "",
        password: "",
        employee: false
    })

    const [wrongData, setWrongData] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        fetch('http://localhost:8080/login', {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(loginData),
            credentials: "include"
        })
        .then( resp => {
            if (resp.status === 200) {
                sessionStorage.setItem("email", loginData.email)
                props.setIsAuthenticated(true)                

                navigate('/')
            } else {
                setWrongData(true)
            }
        })
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {

        setLoginData(prevState => (
            {
                ...prevState,
                [e.target.name]: e.target.value
            }
        )) 
        
    }

    return (
        <section className="loginPage__login-section panel-wrapper__panel--no-image-background">
            <h2 className="panel-wrapper__title">Log In to Your Account</h2>
            <form className="form" onSubmit={handleSubmit}>
                <div className="form__fields">
                    <TextInput 
                        name="email" 
                        state={loginData.email} 
                        setState={handleChange} 
                        isRequired 
                        placeholder="Email"
                    />
                    <PasswordInput
                        name="password" 
                        state={loginData.password} 
                        setState={handleChange} 
                        placeholder="Password" 
                     />            
                    {wrongData && <p className="incorrect-data-text">Incorrect email or password</p>}
                </div>
                <button className="btn btn--pink btn--greater btn--greater-border-radius">Log In</button>
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