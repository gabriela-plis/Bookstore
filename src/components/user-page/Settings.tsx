import { useEffect, useState } from "react";
import User from "../../DTO/User";
import useFetch from "../../functions/useFetch";
import TextInput from "../../reusable-components/TextInput";



const Settings = () => {
    const userDTO = useFetch('http://localhost:8000/users/' + sessionStorage.getItem("id")) as unknown as User;

    const [user, setUser] = useState<User>({
        id: 0,
        firstName: "",
        lastName: "",
        phone: "",
        email: "",
        password: "",
        employee: false
    });
   
    const [readyToRender, setReadyToRender] = useState(false);


    useEffect( () => {

        if (Object.keys(userDTO).length !== 0) {

        setUser(userDTO);
        
        setReadyToRender(true);
        }

    },[userDTO])


    const handleEditData = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, setShowEditData: React.Dispatch<React.SetStateAction<boolean>>) => {
        e.preventDefault();

       
        fetch('http://localhost:8000/users/' + sessionStorage.getItem("id"), {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user)
        })

        setShowEditData(false);
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setUser(prevState => (
            {
                ...prevState,
                [e.target.name]: e.target.value
            }
        )) 
    }

    return (
        <section className="settings">
            <h2 className="settings__title">Account settings</h2>
            {readyToRender &&  
                <div className="settings__content">
                    <PersonalInformation data={user.firstName} setData={handleChange} className="first-name" inputName="firstName" displayName="First name" handleEditData={handleEditData}/>
                    <PersonalInformation data={user.lastName} setData={handleChange} className="last-name" inputName="lastName" displayName="Last name" handleEditData={handleEditData}/>
                    <PersonalInformation data={user.email} setData={handleChange} className="email" displayName="Email" handleEditData={handleEditData}/>
                    <PersonalInformation data={user.phone} setData={handleChange} className="phone" displayName="Phone" handleEditData={handleEditData}/>
                    <PersonalInformation data={user.password} setData={handleChange} className="password" displayName="Password" handleEditData={handleEditData} buttonName="Change"/>
                </div>     
            }
            <span className="settings__image"><div></div></span>
        </section>
    )
}

type PersonalInformationProps = {
    data: string;
    setData: (e: React.ChangeEvent<HTMLInputElement>) => void;
    className: string;
    displayName: string;
    handleEditData: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, setShowEditData: React.Dispatch<React.SetStateAction<boolean>>) => void;
    inputName?: string;
    buttonName?: string;
}

const PersonalInformation = (props: PersonalInformationProps) => {

    const[showEditPanel, setShowEditPanel] = useState(false);

    const handleClickEditBtn = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.preventDefault();
        setShowEditPanel(true);
    }

    return (
        <div className = {"settings__data settings__data--"+props.className}>
            <label htmlFor={props.className}>
                <span className="settings__label-description">{props.displayName}:</span>
                {props.data ? (
                    <span>{props.data}</span>
                ) : (
                    <span>- - -</span>
                )} 
            </label>
            <button 
                className="btn btn--edit" 
                onClick={ (e) => {
                    handleClickEditBtn(e)
                }}
            >
               {props.buttonName ? (
                    <span>{props.buttonName}</span>
               ) : (
                    <span>Edit</span>
               )} 
            </button>
            {showEditPanel && (
                    <EditPanel 
                        data={props.data}
                        setData={props.setData} 
                        setShowEditPanel={setShowEditPanel} 
                        displayName={props.displayName} 
                        handleEditData={props.handleEditData} 
                        inputName={props.inputName ? props.inputName : props.className}
                    />
                )
            }
        </div>
    )
}

type EditPanelProps = {
    data: string;
    setData: (e: React.ChangeEvent<HTMLInputElement>) => void;
    setShowEditPanel: React.Dispatch<React.SetStateAction<boolean>>;
    displayName: string;
    inputName: string;
    handleEditData: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, setShowEditData: React.Dispatch<React.SetStateAction<boolean>>) => void;
}

const EditPanel = (props: EditPanelProps) => {
    const [test, setTest] = useState('');

    const handleCancelOperation = () => {
        props.setShowEditPanel(false);
    } 

    return ( 
        <div className="edit__container">
            <section className="edit__content">
                <h2 className="edit__title">Edit {props.displayName}</h2>
                <div className="edit__input-container">
                    <label className="edit__label">{props.displayName}</label>
                    <TextInput 
                        name={props.inputName}
                        state={props.data} 
                        setState={props.setData} 
                        isRequired 
                        placeholder={props.data}

                    />
                </div>
                <button className="btn btn--cancel" onClick={handleCancelOperation}>Cancel</button>
                <button className="btn btn--save" 
                    onClick={ (e) => {
                        props.handleEditData(e, props.setShowEditPanel);
                    }}
                >Save</button>
            </section>
        </div>
     );
}

export default Settings;