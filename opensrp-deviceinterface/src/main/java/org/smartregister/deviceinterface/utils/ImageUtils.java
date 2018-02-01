package org.smartregister.deviceinterface.utils;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.deviceinterface.DeviceInterfaceLibrary;
import org.smartregister.deviceinterface.R;
import org.smartregister.domain.Photo;
import org.smartregister.domain.ProfileImage;

import static org.smartregister.util.Utils.getValue;

/**
 * Created by sid-tech on 2/1/18.
 */

public class ImageUtils {

    public static Photo profilePhotoByClient(CommonPersonObjectClient client) {
        Photo photo = new Photo();
        ProfileImage profileImage = DeviceInterfaceLibrary.getInstance().context().imageRepository().findByEntityId(client.entityId());
        if (profileImage != null) {
            photo.setFilePath(profileImage.getFilepath());
        } else {
            String gender = getValue(client, "gender", true);
            photo.setResourceId(profileImageResourceByGender(gender));
        }
        return photo;
    }

    public static int profileImageResourceByGender(String gender) {
        if (StringUtils.isNotBlank(gender)) {
            if (gender.equalsIgnoreCase("male")) {
                return R.drawable.child_boy_infant;
            } else if (gender.equalsIgnoreCase("female")) {
                return R.drawable.child_girl_infant;
            }
        }
        return R.drawable.child_boy_infant;
    }


}
