import { AbstractControl, ValidationErrors } from "@angular/forms";

export class CustomValidators {

  static usernameValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    const regex = /^[a-zA-Z0-9]+((.|-|_)[a-zA-Z0-9]+)*$/;
    if (!regex.test(value)) {
      return { 
          invalidusername: true 
        };
    }
    
    return null;
  }

  static emailValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    const regex = /^[a-zA-Z0-9]+((.|-|_)[a-zA-Z0-9]+)*@[a-z0-9]+[.][a-z]{2,6}$/;
    if (!regex.test(value)) {
      return { invalidemail: true };
    }
    
    return null;
  }

  static passwordValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    const regex = /^[0-9a-zA-Z_.-@#*$%^&+=]+$/;
    if (!regex.test(value)) {
      return { 
          invalidpassword: true 
        };
    }
    return null;
  }

  static matchPasswords(password: string): (AbstractControl) => ValidationErrors | null {
    return (control: AbstractControl): ValidationErrors | null => {
      return !!control.parent && !!control.parent.value && control.value === control.parent.controls[password].value
        ? null : { matching: false };
    };
}

  static minTotalGuests(guests: string): (AbstractControl) => ValidationErrors | null {
    return (control: AbstractControl): ValidationErrors | null => {
      return !!control.parent && !!control.parent.value && control.value >= control.parent.controls[guests].value
        ? null : { minimum: false };
    };
  }

  static dateNotPast(control: AbstractControl): ValidationErrors | null {
      const value = control.value;
      const now = new Date().toISOString().substring(0, 16);
      if (value < now) {
        return { 
            invaliddate: true 
          };
      }
      return null;
    }


  static endAfterStart(start: string): (AbstractControl) => ValidationErrors | null {
    return (control: AbstractControl): ValidationErrors | null => {
      return !!control.parent && !!control.parent.value && control.value > control.parent.controls[start].value
        ? null : { endafterstart: false };
    };
  }
}
