import { instance } from "./util/instance";

export const oauth2MergeApi = async (user) => {
    let mergeData = {
        isSuccess: false,
        fieldErrors: [
            {
                field: "",
                defaultMessage: ""
            }
        ],
    }
    try {
        const response = await instance.post("/auth/oauth2/merge", user);
        mergeData = {
            isSuccess: true,
        }
    } catch (error) {
        const response = error.response;
        mergeData = {
            isSuccess: false,
        }

        if (typeof (response.data) === 'string') {
            mergeData['errorStatus'] = 'loginError';
            mergeData['error'] = response.data;
        } else {
            mergeData['errorStatus'] = 'fieldError';
            mergeData['error'] = response.data.map(fieldError => ({
                field: fieldError.field,
                defaultMessage: fieldError.defaultMessage
            }));
        }
    }
    return mergeData;
}

export const oauth2JoinApi = async (user) => {
    let joinData = {
        inSuccess: false,
        fieldErrors: [
            {
                field: "",
                defaultMessage: ""
            }
        ],
    }
    try {
        const response = await instance.post("/auth/oauth2/signup", user);
        joinData = {
            isSuccess: true,
        }

    } catch (error) {
        const response = error.response;
        joinData = {
            isSuccess: false
        }
        if (typeof (response.data) === 'string') {
            joinData['errorStatus'] = 'loginError';
            joinData['error'] = response.data;
        } else {
            joinData['errorStatus'] = 'fieldError';
            joinData['error'] = response.data.map(fieldError => ({
                field: fieldError.field,
                defaultMessage: fieldError.defaultMessage
            }));
        }
    }
    return joinData;
}