type PasswordInputProps = {
    state: string;
    setState: (val: string) => void;
    isRequired: boolean;
    placeholder: string;
  };


const PasswordInput = (props: PasswordInputProps) => {
    return ( 
        <input 
            type="password"
            required={props.isRequired}
            value={props.state} 
            onChange={(e) => {props.setState(e.target.value)}}
            placeholder={props.placeholder}
        />
     );
}
 
export default PasswordInput;